package com.mihai.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class CollectionUtilitiesTest {

    @Test
    public void lastElementMatchingConditionIsKept() {
        List<Integer> filteredList = CollectionUtilities.takeUntil(List.of(1, 2, 3, 4, 5), number -> number == 3);
        assertEquals(List.of(1, 2, 3), filteredList);
    }

    @Test
    public void allElementsAreKeptWhenConditionNotMatched() {
        List<Integer> filteredList = CollectionUtilities.takeUntil(List.of(1, 2, 3, 4, 5), number -> number > 10);
        assertEquals(List.of(1, 2, 3, 4, 5), filteredList);
    }
}
