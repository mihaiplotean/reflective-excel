package com.mihai.core.field;

import com.mihai.core.annotation.DynamicColumns;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DynamicColumnFieldTest {

    @Test
    public void invalidTypeParameterThrowsException() {
        assertThrows(IllegalStateException.class,
                () -> new DynamicColumnField(InvalidDynamicColumnType.class.getDeclaredField("value"), null));
    }

    @Test
    public void nestedGenericTypeParameterInDynamicColumnThrowsException() {
        assertThrows(IllegalStateException.class,
                () -> new DynamicColumnField(GenericDynamicColumnType.class.getDeclaredField("value"), null));
    }

    private static class InvalidDynamicColumnType {

        @DynamicColumns
        private Integer value;
    }

    private static class GenericDynamicColumnType {

        @DynamicColumns
        private List<List<String>> value;
    }
}