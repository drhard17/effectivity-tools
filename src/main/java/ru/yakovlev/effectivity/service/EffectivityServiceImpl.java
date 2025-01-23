package ru.yakovlev.effectivity.service;

import java.util.List;
import java.util.stream.Collectors;
import ru.yakovlev.effectivity.exception.EffectivityException;
import ru.yakovlev.effectivity.model.Effectivity;
import ru.yakovlev.effectivity.model.EndItem;
import ru.yakovlev.effectivity.model.EndItemSet;
import ru.yakovlev.effectivity.model.UnitRange;
import static ru.yakovlev.effectivity.service.UnitRangeServiceImpl.parseTcUnitRanges;
import static ru.yakovlev.effectivity.service.EndItemSetServiceImpl.convertEndItemSetUnitRangesToSequence;
import static ru.yakovlev.effectivity.service.EndItemSetServiceImpl.parseJournalUnit;

public class EffectivityServiceImpl {

    /**
     * Преобразует применяемость в формате Teamcenter в объект Effectivity
     * @param tcString - значение из Teamcenter, пример - "1, 3, 4 (MC-21) 99011 (ST-21)"
     * @return объект Effectivity
     */

    public static Effectivity parseTcString(String tcString) {
        if (tcString.isEmpty()) {
            return new Effectivity();
        }

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
        if (journalString.isBlank()) {
            return new Effectivity();
        }

        String deWhiteSpacedString = journalString.replaceAll("\\s", ";")
                .replaceAll(",", ";")
                .replaceAll(";+", ";");
        
        String[] journalUnits = deWhiteSpacedString.split(";");
        EndItem[] endItems = { EndItem.MC21, EndItem.ST21 };

        Effectivity effectivity = new Effectivity();
        
        for (String journalUnit : journalUnits) {
            sumEffectivityWithUnitRange(effectivity, parseJournalUnit(journalUnit, endItems));
        }

        return effectivity;
    }

    /**
     * Преобразует объект Effectivity в строку формата журнала CMD
     * @return
     */

    public static String toJournalString(Effectivity effectivity) {
        return effectivity.getEndItemSets().stream()
                .map(set -> convertEndItemSetUnitRangesToSequence(set))
                .sorted()
                .collect(Collectors.joining("; "));
    }

    private static Effectivity sumEffectivityWithUnitRange(Effectivity effectivity,
            EndItemSet endItemSetToSum) {
        if (endItemSetToSum.getUnitRanges().size() != 1) {
            throw new EffectivityException("Wrong size of unitrange");
        }
    
        EndItem endItemToSum = endItemSetToSum.getEndItem();
        List<UnitRange> unitRangesToSum = endItemSetToSum.getUnitRanges();
    
        effectivity.getEndItemSets().stream()
            .filter(existingSet -> existingSet.getEndItem().equals(endItemToSum))
            .findFirst()
            .ifPresentOrElse(
                existingSet -> existingSet.addUnitRange(unitRangesToSum.get(0)),
                () -> effectivity.addEndItemSet(endItemSetToSum)
            );
    
        return effectivity;
    }
}
