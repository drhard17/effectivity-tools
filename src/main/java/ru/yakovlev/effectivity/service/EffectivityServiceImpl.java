package ru.yakovlev.effectivity.service;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import ru.yakovlev.effectivity.model.Effectivity;
import ru.yakovlev.effectivity.model.EndItem;
import ru.yakovlev.effectivity.model.EndItemSet;

public class EffectivityServiceImpl {

    static final String UP_LITERAL = "-UP";
    private static final String JOURNAL_DELIMITER = "; ";
    private static final String JOURNAL_NUM_FORMAT = "%04d";

        /**
     * Преобразует применяемость в формате Teamcenter в объект Effectivity
     * @param tcString - значение из Teamcenter, пример - "1, 3, 4 (MC-21) 99011 (ST-21)"
     * @return объект Effectivity
     */

    public static Effectivity parseTcString(String tcString) {

        String[] tokens = tcString.split(" \\(|\\) |\\)");
        if (tokens.length < 2 || tokens.length % 2 != 0) {
            throw new IllegalArgumentException("Wrong TC effectivity string - " + tcString);
        }
        
        Effectivity effectivity = new Effectivity();
       
        for (int i = 0; i < tokens.length - 1; i = i + 2) {
            String rangeToken = tokens[i];
            String endItemToken = tokens[i + 1];
            
            String range = rangeToken.replace(UP_LITERAL, "");
            TreeSet<Integer> serialNumbers = RangeConverter.convertFromRanges(range);
            EndItem endItem = EndItem.fromAlias(endItemToken);
            boolean isUP = rangeToken.contains(UP_LITERAL);

            EndItemSet endItemSet = new EndItemSet(endItem, serialNumbers, isUP);
            addSetToEffectivity(effectivity, endItemSet);
        }
        return effectivity;
    }

    /**
     * Преобразует применяемость в формате журнала CMD в объект Effectivity
     * @param journalString - значение из журнала, пример - "0001; 0012; 99012"
     * @return объект Effectivity
     */

    public static Effectivity parseJournalString(String journalString) {

        Effectivity effectivity = new Effectivity();

        Arrays.stream(EndItem.values())
                .map(endItem -> EndItemSetServiceImpl.parseJournalEndItemSet(journalString, endItem))
                .filter(set -> set != null)
                .forEach(endItemSet -> addSetToEffectivity(effectivity, endItemSet));

        return effectivity;
    }

    /**
     * Преобразует объект Effectivity в строку формата журнала CMD
     * @return
     */

    public static String toJournalString(Effectivity effectivity) {
        return effectivity.getEndItemSets().stream()
                .map(set -> {
                    String endItemList = set.getSerialNumbers().stream()
                            .map(serialNumber -> String.format(JOURNAL_NUM_FORMAT, serialNumber))
                            .collect(Collectors.joining(JOURNAL_DELIMITER));
                    return set.getIsUp() ? endItemList + UP_LITERAL : endItemList;
                })
                .sorted()
                .collect(Collectors.joining(JOURNAL_DELIMITER));
    }

    // TODO убрать trimUp
    private static Effectivity addSetToEffectivity(Effectivity effectivity, EndItemSet endItemSet) {

        List<EndItemSet> endItemSets = effectivity.getEndItemSets();
        endItemSets.add(EndItemSetServiceImpl.trimUpEndItemSet(endItemSet));

        return new Effectivity(endItemSets);
    }

}
