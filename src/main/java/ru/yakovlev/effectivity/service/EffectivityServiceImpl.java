package ru.yakovlev.effectivity.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import ru.yakovlev.effectivity.exception.EffectivityException;
import ru.yakovlev.effectivity.model.Effectivity;
import ru.yakovlev.effectivity.model.EndItem;
import ru.yakovlev.effectivity.model.EndItemSet;
import ru.yakovlev.effectivity.model.UnitRange;
import static ru.yakovlev.effectivity.service.EndItemSetServiceImpl.parseTcUnitRanges;
import static ru.yakovlev.effectivity.service.EndItemSetServiceImpl.converEndItemSetUnitRangesToSequence;
import static ru.yakovlev.effectivity.service.EndItemSetServiceImpl.parseEndItemSetfromJournalString;

public class EffectivityServiceImpl {

    static final String UP_LITERAL = "-UP";

        /**
     * Преобразует применяемость в формате Teamcenter в объект Effectivity
     * @param tcString - значение из Teamcenter, пример - "1, 3, 4 (MC-21) 99011 (ST-21)"
     * @return объект Effectivity
     */

    public static Effectivity parseTcString(String tcString) {
        String[] tokens = tcString.split(" \\(|\\) |\\)");
        if (tokens.length < 2 || tokens.length % 2 != 0) {
            throw new EffectivityException("Wrong TC effectivity string - " + tcString);
        }
        
        Effectivity effectivity = new Effectivity();
       
        for (int i = 0; i < tokens.length - 1; i = i + 2) {
            EndItem endItem = EndItem.fromAlias(tokens[i + 1]);
            List<UnitRange> unitRanges = parseTcUnitRanges(tokens[i]);
            EndItemSet endItemSet = new EndItemSet(endItem, unitRanges);
            effectivity.addEndItemSet(endItemSet);
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
                .map(endItem -> parseEndItemSetfromJournalString(journalString, endItem))
                .filter(set -> set.getUnitRanges().size() != 0)
                .forEach(endItemSet -> effectivity.addEndItemSet(endItemSet));

        return effectivity;
    }

    /**
     * Преобразует объект Effectivity в строку формата журнала CMD
     * @return
     */

    public static String toJournalString(Effectivity effectivity) {
        return effectivity.getEndItemSets().stream()
                .map(set -> converEndItemSetUnitRangesToSequence(set))
                .sorted()
                .collect(Collectors.joining("; "));
    }

}
