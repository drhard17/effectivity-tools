package ru.yakovlev.effectivity.service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ru.yakovlev.effectivity.model.EndItem;
import ru.yakovlev.effectivity.model.EndItemSet;
import ru.yakovlev.effectivity.model.UnitRange;

public class EndItemSetServiceImpl {

    static List<UnitRange> parseTcUnitRanges(String tcRanges) {
        return Arrays.stream(tcRanges.split(", "))
                .map(r -> parseUnitRange(r))
                .collect(Collectors.toList());
    }

    private static UnitRange parseUnitRange(String range) {
        String[] bounds = range.split("-");
        Integer start = Integer.parseInt(bounds[0]);

        if (bounds.length == 2) {
            if (bounds[1].equals("UP")) {
                return new UnitRange(start, null);
            } else {
                Integer end = Integer.parseInt(bounds[1]);
                return new UnitRange(start, end);
            }
        }

        return new UnitRange(start, start);
    }

    static EndItemSet parseEndItemSetfromJournalString(String journalString,
            EndItem endItem) {

        EndItemSet endItemSet = new EndItemSet(endItem);

        Matcher matcher = Pattern.compile(endItem.getJournalPattern()).matcher(journalString);
        while (matcher.find()) {
            String unit = matcher.group();
            UnitRange unitRange = parseUnitRange(unit);
            endItemSet.addUnitRange(unitRange);
        }
        return endItemSet;
    }

    static String convertUnitRangeToSequence(UnitRange range, String numFormat, String delimiter) {
        if (range.isOpenRange()) {
            return String.format(numFormat, range.getStart()) + "-UP";
        }
        return IntStream.rangeClosed(range.getStart(), range.getEnd())
                .mapToObj(num -> String.format(numFormat, num))
                .collect(Collectors.joining(delimiter));
    }

    static String converEndItemSetUnitRangesToSequence(EndItemSet endItemSet) {
        final String delimiter = "; ";
        final String numFormat = "%04d";
        return endItemSet.getUnitRanges().stream()
                .map(range -> convertUnitRangeToSequence(range, numFormat, delimiter))
                .sorted()
                .collect(Collectors.joining(delimiter));
    }

}
