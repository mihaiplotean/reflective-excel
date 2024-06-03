package com.mihai.reader.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import com.mihai.core.annotation.ExcelCellValue;
import com.mihai.core.annotation.ExcelProperty;
import com.mihai.core.annotation.TableId;
import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ExcelReadingTest;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.readers.TableReaderTest.TestRow;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;

class ObjectReaderTest extends ExcelReadingTest {

    @Test
    public void cellValuesCorrectlyRead() {
        createRow(0).createCell(0).setCellValue("test value");
        createRow(1).createCell(1).setCellValue(42);

        ReadableSheetContext sheetContext = createSheetContext();
        ObjectReader reader = new ObjectReader(sheetContext, ExcelReadingSettings.DEFAULT);

        TestCellValues cellValues = reader.read(TestCellValues.class);
        assertEquals("test value", cellValues.valueA);
        assertEquals(42, cellValues.valueB);
    }

    @Test
    public void cellPropertyValuesCorrectlyRead() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("key");
        row.createCell(1).setCellValue("test value");

        ReadableSheetContext sheetContext = createSheetContext();
        ObjectReader reader = new ObjectReader(sheetContext, ExcelReadingSettings.DEFAULT);

        TestCellPropertyValues cellValues = reader.read(TestCellPropertyValues.class);
        assertEquals("test value", cellValues.valueA);
    }

    @Test
    public void readingPropertyThrowsExceptionOnIncorrectName() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("not key");
        row.createCell(1).setCellValue("test value");

        ReadableSheetContext sheetContext = createSheetContext();
        ObjectReader reader = new ObjectReader(sheetContext, ExcelReadingSettings.DEFAULT);

        assertThrows(BadInputException.class, () -> reader.read(TestCellPropertyValues.class));
    }

    @Test
    public void tableRowsCorrectlyRead() {
        Row row1 = createRow(0);
        row1.createCell(0).setCellValue("Column A");
        row1.createCell(1).setCellValue("Column B");
        Row row2 = createRow(1);
        row2.createCell(0).setCellValue("value A");
        row2.createCell(1).setCellValue("value B");

        ReadableSheetContext sheetContext = createSheetContext();
        ObjectReader reader = new ObjectReader(sheetContext, ExcelReadingSettings.DEFAULT);

        TestTableRowValues rowValues = reader.read(TestTableRowValues.class);
        assertEquals("table 1", sheetContext.getReadingContext().getLastReadTable().getId());
        assertEquals(List.of(new TestRow("value A", "value B")), rowValues.valueList);
    }

    @Test
    public void annotatingWrongFieldAsTableThrowsException() {
        ReadableSheetContext sheetContext = createSheetContext();
        ObjectReader reader = new ObjectReader(sheetContext, ExcelReadingSettings.DEFAULT);

        assertThrows(IllegalStateException.class, () -> reader.read(TestTableRowValuesIncorrectAnnotatedType.class));
    }

    public static class TestCellValues {

        @ExcelCellValue(cellReference = "A1")
        private String valueA;

        @ExcelCellValue(cellReference = "B2")
        private int valueB;
    }

    public static class TestCellPropertyValues {

        @ExcelProperty(name = "key", nameReference = "A1", valueReference = "B1")
        private String valueA;
    }

    public static class TestTableRowValues {

        @TableId("table 1")
        private List<TestRow> valueList;
    }

    public static class TestTableRowValuesIncorrectAnnotatedType {

        @TableId("table 1")
        private TestRow row;
    }
}
