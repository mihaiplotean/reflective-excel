package com.reflectiveexcel.core.field;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import com.reflectiveexcel.core.annotation.DynamicColumns;
import org.junit.jupiter.api.Test;

public class DynamicColumnFieldTest {

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
