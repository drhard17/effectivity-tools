package ru.yakovlev.effectivity.model;

import java.util.TreeSet;

/**
 * Представляет набор серийных номеров для конкретного конечного изделия.
 * Массив EndItemSet образует объект Effectivity
 *   
 */

public class EndItemSet {

    EndItem endItem;

    TreeSet<Integer> serialNumbers;

    boolean isUP = false;

    public EndItemSet(EndItem endItem, TreeSet<Integer> serialNumbers, boolean isUP) {
        this.endItem = endItem;
        this.serialNumbers = serialNumbers;
        this.isUP = isUP;
    }

    public EndItem getEndItem() {
        return endItem;
    }

    public TreeSet<Integer> getSerialNumbers() {
        return serialNumbers;
    }

    public boolean getIsUp() {
        return isUP;
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
        return this.endItem.equals(endItemSet.endItem) &&
                this.serialNumbers.equals(endItemSet.serialNumbers) &&
                this.isUP == endItemSet.isUP;
    }

    boolean containsSet(EndItemSet endItemSet) {
        if (!this.endItem.equals(endItemSet.endItem))
            return false;
        if (!this.isUP && endItemSet.isUP)
            return false;

        TreeSet<Integer> setToCheck = endItemSet.serialNumbers;
        TreeSet<Integer> sourceSet = extendTo(this.serialNumbers, setToCheck.last());

        return sourceSet.containsAll(setToCheck);
    }

    private TreeSet<Integer> extendTo(TreeSet<Integer> set, int expandTo) {

        int expandFrom = set.last();
        if (expandFrom >= expandTo) {
            return set;
        }

        TreeSet<Integer> resultSet = new TreeSet<>(set);

        for (int i = expandFrom + 1; i <= expandTo; i++) {
            resultSet.add(i);
        }

        return resultSet;
    }
}