package ru.yakovlev.effectivity;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

class RangeConverter {
    // Converts a list of integers into a string with ranges
    static String convertToRanges(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int start = numbers.get(0);
        int end = start;

        for (int i = 1; i <= numbers.size(); i++) {
            if (i < numbers.size() && numbers.get(i) == end + 1) {
                end = numbers.get(i);
            } else {
                if (start == end) {
                    result.append(start);
                } else {
                    result.append(start).append("-").append(end);
                }
                if (i < numbers.size()) {
                    result.append(", ");
                    start = numbers.get(i);
                    end = start;
                }
            }
        }

        return result.toString();
    }

    // Converts a string with ranges into a number list
    static TreeSet<Integer> convertFromRanges(String input) {
        TreeSet<Integer> numbers = new TreeSet<>();
        if (input == null || input.isEmpty()) {
            return numbers;
        }

        String[] parts = input.split(", ");
        for (String part : parts) {
            if (part.contains("-")) {
                String[] range = part.split("-");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (int i = start; i <= end; i++) {
                    numbers.add(i);
                }
            } else {
                numbers.add(Integer.parseInt(part));
            }
        }

        return numbers;
    }

    // Обрезает последовательное окончание для UP-диапазона
    static TreeSet<Integer> trimConsecutiveEnd(TreeSet<Integer> set) {
        if (set.isEmpty() || set.size() < 2) {
            return set;
        }

        Iterator<Integer> descendingIterator = set.descendingIterator();
        int last = descendingIterator.next();
        int current;
        
        while (descendingIterator.hasNext()) {
            current = descendingIterator.next();
            if (current + 1 != last) {
                break;
            }
            last = current;
        }

        return new TreeSet<>(set.headSet(last, true));
    }

    // Продлевает множество от конечного элемента до заданного включительно
    static TreeSet<Integer> expandSet(TreeSet<Integer> set, int expandTo) {

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
