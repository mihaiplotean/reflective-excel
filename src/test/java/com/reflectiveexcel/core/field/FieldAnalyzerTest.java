package com.reflectiveexcel.core.field;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.reflectiveexcel.core.annotation.ExcelColumn;
import org.junit.jupiter.api.Test;

public class FieldAnalyzerTest {

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
