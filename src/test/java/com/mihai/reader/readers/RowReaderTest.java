package com.mihai.reader.readers;

import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.deserializer.DefaultDeserializationContext;
import com.mihai.reader.mapping.HeaderMappedField;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class RowReaderTest {

    private XSSFWorkbook workbook;
    private XSSFSheet actualSheet;
    private ReadableSheet sheet;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        actualSheet = workbook.createSheet();
        sheet = new ReadableSheet(actualSheet);
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void pojoValuesEqualColumnValuesOfRow() throws NoSuchFieldException {
        Row row = actualSheet.createRow(0);
        row.createCell(1).setCellValue("test");
        row.createCell(2).setCellValue(42);

        ColumnFieldTestMapping columnFieldMapping = new ColumnFieldTestMapping(Map.of(
                1, new HeaderMappedTestField(TestDummyRow.class.getDeclaredField("stringValue")),
                2, new HeaderMappedTestField(TestDummyRow.class.getDeclaredField("intValue"))
        ));

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentRow(0);
        RowReader rowReader = new RowReader(sheetContext, columnFieldMapping);
        TestDummyRow readRow = rowReader.readRow(sheetContext.getCurrentRow(), TestDummyRow.class);

        assertEquals("test", readRow.stringValue);
        assertEquals(42, readRow.intValue);
    }

    @Test
    public void missingFieldIsIgnored() {
        Row row = actualSheet.createRow(0);
        row.createCell(1).setCellValue("test");

        ColumnFieldTestMapping columnFieldMapping = new ColumnFieldTestMapping(Map.of());

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
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
