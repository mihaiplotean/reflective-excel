package com.mihai.core.field;

import com.mihai.core.annotation.ExcelColumn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FieldAnalyzerTest {

    @Test
    public void duplicateFixedColumnNamesThrowsException() {
        assertThrows(IllegalStateException.class, () -> new FieldAnalyzer(DuplicateColumns.class).getFixedColumnFields());
    }

    public static class DuplicateColumns {

        @ExcelColumn("test")
        private String valueA;

        @ExcelColumn("test")
        private String valueB;
    }
}