package com.reflectiveexcel.core.utils;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class CollectionUtilities {

    private CollectionUtilities() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> List<T> takeUntil(List<T> list, Predicate<T> stopCondition) {
        int lastIndex = IntStream.range(0, list.size())
                .filter(index -> stopCondition.test(list.get(index)))
                .findFirst()
                .orElse(list.size() - 1);
        return list.subList(0, lastIndex + 1);
    }

    public static Collector<String, ?, Set<String>> toCaseInsensitiveSetCollector() {
        return Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
    }
}
