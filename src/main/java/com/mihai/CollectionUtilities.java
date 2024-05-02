package com.mihai;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class CollectionUtilities {

    private CollectionUtilities() {
        throw new IllegalStateException("Utility class");
    }

    public static Collector<String, ?, Set<String>> toCaseInsensitiveSetCollector() {
        return Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
    }
}
