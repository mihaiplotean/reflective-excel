package com.mihai.core.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectionUtilitiesTest {

    @Test
    public void missingEmptyConstructorThrowsException() {
        assertThrows(IllegalStateException.class, () -> ReflectionUtilities.newObject(NoEmptyConstructor.class));
    }

//    @Test
//    public void readingFinalFieldThrowsException() {
//        assertThrows(IllegalStateException.class, () -> ReflectionUtilities.readField(
//                FinalField.class.getDeclaredField("value"), new FinalField(1)
//        ));
//    }
//
//    @Test
//    public void writingFinalFieldThrowsException() {
//        assertThrows(IllegalStateException.class, () -> ReflectionUtilities.writeField(
//                FinalField.class.getDeclaredField("value"), new FinalField(1), 42
//        ));
//    }

    private static class NoEmptyConstructor {

        public NoEmptyConstructor(Integer value) {
        }
    }
}