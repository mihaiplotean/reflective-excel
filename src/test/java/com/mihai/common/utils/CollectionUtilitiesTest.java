package com.mihai.common.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilitiesTest {

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
