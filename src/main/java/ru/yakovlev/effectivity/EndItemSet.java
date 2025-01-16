package ru.yakovlev.effectivity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Представляет набор серийных номеров для конкретного конечного изделия.
 * Массив EndItemSet образует объект Effectivity
 *   
 */

class EndItemSet {

    EndItem endItem;

    TreeSet<Integer> serialNumbers;

    boolean isUP = false;

    EndItemSet(EndItem endItem, TreeSet<Integer> serialNumbers, boolean isUP) {
        this.endItem = endItem;
        this.serialNumbers = serialNumbers;
        this.isUP = isUP;
    }

    boolean containsSet(EndItemSet endItemSet) {
        if (!this.endItem.equals(endItemSet.endItem))
            return false;
        if (!this.isUP && endItemSet.isUP)
            return false;

        TreeSet<Integer> setToCheck = endItemSet.serialNumbers;
        TreeSet<Integer> sourceSet =
                RangeConverter.expandSet(this.serialNumbers, setToCheck.last());

        return sourceSet.containsAll(setToCheck);
    }

    EndItemSet trimUp() {
        if (!this.isUP) {
            return this;
        }
        TreeSet<Integer> serialNumbers = RangeConverter.trimConsecutiveEnd(this.serialNumbers);
        return new EndItemSet(this.endItem, serialNumbers, true);
    }

    static EndItemSet parseJournalEndItemSet(String journalString, EndItem endItem) {
        
        List<Integer> journalSerialNumbers = new ArrayList<>();
        Matcher matcher = Pattern.compile(endItem.getJournalPattern())
                .matcher(journalString);
        while (matcher.find()) {
            journalSerialNumbers.add(Integer.parseInt(matcher.group()));
        }

        if (journalSerialNumbers.size() == 0) {
            return null;
        }

        TreeSet<Integer> serialNumbersSet = new TreeSet<>(journalSerialNumbers);
        boolean isUP = checkIsUPforEndItem(journalString, endItem);

        return new EndItemSet(endItem, serialNumbersSet, isUP);
    }

    private static boolean checkIsUPforEndItem(String journalString, EndItem endItem) {

        String journalPattern = endItem.getJournalPattern();
        String upPattern = journalPattern + Effectivity.UP_LITERAL;
        Matcher matcher = Pattern.compile(upPattern)
                .matcher(journalString);
        
        return matcher.find();
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
}