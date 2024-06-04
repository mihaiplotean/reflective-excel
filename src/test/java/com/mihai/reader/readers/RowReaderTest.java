package com.mihai.reader.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import com.mihai.reader.ExcelReadingTest;
import com.mihai.reader.ReadableSheetContext;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;


public class RowReaderTest extends ExcelReadingTest {

    @Test
    public void pojoValuesEqualColumnValuesOfRow() throws NoSuchFieldException {
        Row row = createRow(0);
        row.createCell(1).setCellValue("test");
        row.createCell(2).setCellValue(42);

        ColumnFieldTestMapping columnFieldMapping = new ColumnFieldTestMapping(Map.of(
                1, new HeaderMappedTestField(TestDummyRow.class.getDeclaredField("stringValue")),
                2, new HeaderMappedTestField(TestDummyRow.class.getDeclaredField("intValue"))
        ));

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentRow(0);
        RowReader rowReader = new RowReader(sheetContext, columnFieldMapping);
        TestDummyRow readRow = rowReader.readRow(sheetContext.getCurrentRow(), TestDummyRow.class);

        assertEquals("test", readRow.stringValue);
        assertEquals(42, readRow.intValue);
    }

    @Test
    public void missingFieldIsIgnored() {
        Row row = createRow(0);
        row.createCell(1).setCellValue("test");

        ColumnFieldTestMapping columnFieldMapping = new ColumnFieldTestMapping(Map.of());

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentRow(0);
        RowReader rowReader = new RowReader(sheetContext, columnFieldMapping);
        TestDummyRow readRow = rowReader.readRow(sheetContext.getCurrentRow(), TestDummyRow.class);

        assertNull(readRow.stringValue);
    }

    public static class TestDummyRow {

        private String stringValue;
        private int intValue;

        public TestDummyRow() {
        }
    }
}
