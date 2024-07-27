package com.reflectiveexcel.core.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ReflectionUtilitiesTest {

    @Test
    public void missingEmptyConstructorThrowsException() {
        assertThrows(IllegalStateException.class, () -> ReflectionUtilities.newObject(NoEmptyConstructor.class));
    }

    private static class NoEmptyConstructor {

        public NoEmptyConstructor(Integer value) {
        }
    }
}
