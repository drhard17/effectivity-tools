package ru.yakovlev.effectivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Класс для хранения и операций с применяемостью - набором серийных номеров конечных изделий
 */

public class Effectivity {

    static final String UP_LITERAL = "-UP";
    private static final String JOURNAL_DELIMITER = "; ";
    private static final String JOURNAL_NUM_FORMAT = "%04d";
    
    private final List<EndItemSet> endItemSets;

    private Effectivity() {
        this.endItemSets = new ArrayList<>();
    }

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
            effectivity.addSet(endItemSet);
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
                .map(endItem -> EndItemSet.parseJournalEndItemSet(journalString, endItem))
                .filter(set -> set != null)
                .forEach(effectivity::addSet);

        return effectivity;
    }

    /**
     * Преобразует объект Effectivity в строку формата журнала CMD
     * @return
     */

    public String toJournalString() {
        return this.endItemSets.stream()
                .map(set -> {
                    String endItemList = set.serialNumbers.stream()
                            .map(serialNumber -> String.format(JOURNAL_NUM_FORMAT, serialNumber))
                            .collect(Collectors.joining(JOURNAL_DELIMITER));
                    return set.isUP ? endItemList + UP_LITERAL : endItemList;
                })
                .sorted()
                .collect(Collectors.joining(JOURNAL_DELIMITER));
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
    
    private boolean containsEndItemSet(EndItemSet endItemSet) {
        return this.endItemSets.stream()
                .anyMatch(set -> 
                        set.endItem == endItemSet.endItem && 
                        set.containsSet(endItemSet));
    }
    
    private void addSet(EndItemSet endItemSet) {
        this.endItemSets.add(endItemSet.trimUp());
    }
}


  
