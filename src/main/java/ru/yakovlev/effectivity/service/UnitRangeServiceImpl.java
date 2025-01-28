package ru.yakovlev.effectivity.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ru.yakovlev.effectivity.model.UnitRange;

public class UnitRangeServiceImpl {

    static String convertUnitRangeToSequence(UnitRange range, String numFormat, String delimiter) {
        if (range.isOpenRange()) {
            return String.format(numFormat, range.getStart()) + "-UP";
        }
        return IntStream.rangeClosed(range.getStart(), range.getEnd())
                .mapToObj(num -> String.format(numFormat, num))
                .collect(Collectors.joining(delimiter));
    }

    static List<UnitRange> parseTcUnitRanges(String tcRanges) {
        return Arrays.stream(tcRanges.replace(" ", "")
                .split(","))
                .map(r -> parseSingleUnitRange(r))
                .collect(Collectors.toList());
    }

    static UnitRange parseSingleUnitRange(String range) {
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

}
