package ru.yakovlev.effectivity.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;

/**
 * Представляет набор диапазонов серийных номеров для конечного изделия,
 * определенного в EndItem 
 * Массив EndItemSet образует объект Effectivity.
 */

@Getter
public class EndItemSet {

    EndItem endItem;

    List<UnitRange> unitRanges;

    public EndItemSet(EndItem endItem) {
        this.endItem = endItem;
        this.unitRanges = new ArrayList<>();
    }

    public EndItemSet(EndItem endItem, List<UnitRange> unitRanges) {
        this.endItem = endItem;
        this.unitRanges = mergeUnitRanges(unitRanges);
    }

    public void addUnitRange(UnitRange newRange) {
        this.unitRanges.add(newRange);
        this.unitRanges = mergeUnitRanges(this.unitRanges);
    }

    private static List<UnitRange> mergeUnitRanges(List<UnitRange> ranges) {
        if (ranges == null || ranges.isEmpty()) {
            return Collections.emptyList();
        }

        ranges.sort(Comparator.comparingInt(r -> r.getStart()));

        List<UnitRange> mergedRanges = new ArrayList<>();
        UnitRange currentRange = ranges.get(0);

        for (int i = 1; i < ranges.size(); i++) {
            UnitRange nextRange = ranges.get(i);

            if (currentRange.getEnd() == null) {
                continue;
            } else if (nextRange.getEnd() == null
                    && nextRange.getStart() <= currentRange.getEnd() + 1) {
                currentRange.setEnd(null);
            } else if (currentRange.getEnd() >= nextRange.getStart() - 1) {
                currentRange.setEnd(Math.max(currentRange.getEnd(), nextRange.getEnd()));
            } else {
                mergedRanges.add(currentRange);
                currentRange = nextRange;
            }
        }

        mergedRanges.add(currentRange);
        return mergedRanges;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        EndItemSet endItemSet = (EndItemSet) obj;
        return this.endItem.equals(endItemSet.endItem) 
                && this.unitRanges.equals(endItemSet.unitRanges);
    }

    public boolean contains(EndItemSet endItemSet) {
        if (!this.endItem.equals(endItemSet.endItem)) {
            return false;
        }
        return endItemSet.getUnitRanges().stream()
                .allMatch(r -> containsUnitRange(r));
    }

    private boolean containsUnitRange(UnitRange unitRange) {
        return this.unitRanges.stream()
                .anyMatch(r -> r.contains(unitRange));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return  this.unitRanges.toString() + " (" + endItem + ")";
    }

}