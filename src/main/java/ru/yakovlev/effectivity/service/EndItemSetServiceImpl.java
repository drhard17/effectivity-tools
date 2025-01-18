package ru.yakovlev.effectivity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.yakovlev.effectivity.model.EndItem;
import ru.yakovlev.effectivity.model.EndItemSet;

public class EndItemSetServiceImpl {

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
        String upPattern = journalPattern + EffectivityServiceImpl.UP_LITERAL;
        Matcher matcher = Pattern.compile(upPattern)
                .matcher(journalString);
        
        return matcher.find();
    }

    static EndItemSet trimUpEndItemSet(EndItemSet endItemSet) {
        if (!endItemSet.getIsUp()) {
            return endItemSet;
        }
        TreeSet<Integer> serialNumbers = RangeConverter.trimConsecutiveEnd(endItemSet.getSerialNumbers());
        return new EndItemSet(endItemSet.getEndItem(), serialNumbers, true);
    }

}
