package ru.yakovlev.effectivity.service;

import static ru.yakovlev.effectivity.service.UnitRangeServiceImpl.convertUnitRangeToSequence;
import static ru.yakovlev.effectivity.service.UnitRangeServiceImpl.parseSingleUnitRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import ru.yakovlev.effectivity.exception.EffectivityException;
import ru.yakovlev.effectivity.model.EndItem;
import ru.yakovlev.effectivity.model.EndItemSet;
import ru.yakovlev.effectivity.model.UnitRange;

public class EndItemSetServiceImpl {

    static String convertEndItemSetUnitRangesToSequence(EndItemSet endItemSet) {
        final String delimiter = "; ";
        final String numFormat = "%04d";
        return endItemSet.getUnitRanges().stream()
                .map(range -> convertUnitRangeToSequence(range, numFormat, delimiter))
                .sorted()
                .collect(Collectors.joining(delimiter));
    }

    static EndItemSet parseJournalUnit(String unit, EndItem[] endItems) {

        List<EndItemSet> endItemSets = new ArrayList<>();

        for (EndItem endItem : endItems) {
            Matcher matcher = Pattern.compile(endItem.getJournalPattern()).matcher(unit);
            while (matcher.find()) {
                UnitRange unitRange = parseSingleUnitRange(matcher.group());
                EndItemSet endItemSet = new EndItemSet(endItem, Arrays.asList(unitRange));
                endItemSets.add(endItemSet);
            }
        }

        if (endItemSets.size() == 1) {
            return endItemSets.get(0);
        }

        throw new EffectivityException("Wrong journal unit: " + unit);
    }
}
