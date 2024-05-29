package com.mihai.core.field;

import com.mihai.core.annotation.TableId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableIdFieldTest {

    @Test
    public void invalidTypeParameterThrowsException() {
        assertThrows(IllegalStateException.class,
                () -> new TableIdField(InvalidDynamicColumnType.class.getDeclaredField("value"), null));
    }

    @Test
    public void nestedGenericTypeParameterInDynamicColumnThrowsException() {
        assertThrows(IllegalStateException.class,
                () -> new TableIdField(GenericDynamicColumnType.class.getDeclaredField("value"), null));
    }

    private static class InvalidDynamicColumnType {

        @TableId("non-list")
        private Integer value;
    }

    private static class GenericDynamicColumnType {

        @TableId("list of lists")
        private List<List<String>> value;
    }
}