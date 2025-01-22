package ru.yakovlev.effectivity.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс для хранения и операций с применяемостью - набором серийных номеров конечных изделий
 */

@Getter
@AllArgsConstructor
public class Effectivity {

    private final List<EndItemSet> endItemSets;

    public Effectivity() {
        this.endItemSets = new ArrayList<>();
    }

    public void addEndItemSet(EndItemSet endItemSet) {
        this.endItemSets.add(endItemSet);
    }

    /**
     * Проверяет вхождение Effectivity в Effectivity
     * @param effectivity
     * @return
     */

    public boolean contains(Effectivity effectivity) {
        return effectivity.endItemSets.stream().
                allMatch(this::containsEndItemSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Effectivity effectivity = (Effectivity) obj;
        return this.endItemSets.containsAll(effectivity.endItemSets) &&
                effectivity.endItemSets.containsAll(this.endItemSets);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    private boolean containsEndItemSet(EndItemSet endItemSet) {
        return this.endItemSets.stream()
                .anyMatch(set -> 
                        set.endItem == endItemSet.endItem && 
                        set.contains(endItemSet));
    }

}


  
