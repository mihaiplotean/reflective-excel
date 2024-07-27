package com.reflectiveexcel.reader.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Objects;

import com.reflectiveexcel.core.annotation.ExcelColumn;
import com.reflectiveexcel.core.annotation.TableId;
import com.reflectiveexcel.core.workbook.Bounds;
import com.reflectiveexcel.reader.ExcelReadingSettings;
import com.reflectiveexcel.reader.ExcelReadingTest;
import com.reflectiveexcel.reader.ReadableSheetContext;
import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.detector.SimpleRowColumnDetector;
import com.reflectiveexcel.reader.table.ReadTable;
import com.reflectiveexcel.reader.table.TableHeaders;
import com.reflectiveexcel.reader.workbook.sheet.ReadableRow;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;

public class TableReaderTest extends ExcelReadingTest {

    private static final List<TestRow> EXPECTED_ROWS = List.of(
            new TestRow("value A", "value B"),
            new TestRow("value C", "value D")
    );

    @Test
    public void readSimpleTable() {
        Row row1 = createRow(0);
        row1.createCell(0).setCellValue("Column A");
        row1.createCell(1).setCellValue("Column B");
        Row row2 = createRow(1);
        row2.createCell(0).setCellValue("value A");
        row2.createCell(1).setCellValue("value B");
        Row row3 = createRow(2);
        row3.createCell(0).setCellValue("value C");
        row3.createCell(1).setCellValue("value D");

        ReadableSheetContext sheetContext = createSheetContext();
        TableReader tableReader = new TableReader(sheetContext, ExcelReadingSettings.DEFAULT);
        List<TestRow> testRows = tableReader.readRows(TestRow.class);

        assertEquals(EXPECTED_ROWS, testRows);
        ReadTable readTable = sheetContext.getReadingContext().getLastReadTable();
        assertEquals("test-table", readTable.getId());
        TableHeaders readHeaders = readTable.getHeaders();
        assertEquals("Column A", readHeaders.getHeader(0).getValue());
        assertEquals("Column B", readHeaders.getHeader(1).getValue());
        assertEquals(new Bounds(0, 0, 2, 1), readTable.getBounds());
    }

    @Test
    public void noRowsReadWhenAllRowsSkipped() {
        Row row1 = createRow(0);
        row1.createCell(0).setCellValue("Column A");
        row1.createCell(1).setCellValue("Column B");
        Row row2 = createRow(1);
        row2.createCell(0).setCellValue("value A");
        row2.createCell(1).setCellValue("value B");
        Row row3 = createRow(2);
        row3.createCell(0).setCellValue("value C");
        row3.createCell(1).setCellValue("value D");

        ExcelReadingSettings skipAllRowsSettings = ExcelReadingSettings.builder()
                .rowColumnDetector(new SimpleRowColumnDetector("A1") {
                    @Override
                    public boolean shouldSkipRow(ReadingContext context, ReadableRow tableRow) {
                        return true;
                    }
                })
                .build();
        ReadableSheetContext sheetContext = createSheetContext();
        TableReader tableReader = new TableReader(sheetContext, skipAllRowsSettings);
        List<TestRow> testRows = tableReader.readRows(TestRow.class);

        assertEquals(List.of(), testRows);
    }

    @Test
    public void emptyListReturnedWhenNoRowsToRead() {
        Row headerRow = createRow(0);
        headerRow.createCell(0).setCellValue("Column A");
        headerRow.createCell(1).setCellValue("Column B");

        ReadableSheetContext sheetContext = createSheetContext();
        TableReader tableReader = new TableReader(sheetContext, ExcelReadingSettings.DEFAULT);
        List<TestRow> testRows = tableReader.readRows(TestRow.class);

        assertEquals(List.of(), testRows);
    }

    @Test
    public void noRowsReadWhenNoHeadersFound() {
        ReadableSheetContext sheetContext = createSheetContext();
        TableReader tableReader = new TableReader(sheetContext, ExcelReadingSettings.DEFAULT);
        List<TestRow> testRows = tableReader.readRows(TestRow.class);

        assertEquals(List.of(), testRows);
    }

    @TableId("test-table")
    public static class TestRow {

        @ExcelColumn("Column A")
        private String valueA;

        @ExcelColumn("Column B")
        private String valueB;

        public TestRow() {
        }

        public TestRow(String valueA, String valueB) {
            this.valueA = valueA;
            this.valueB = valueB;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            TestRow testRow = (TestRow) object;
            return Objects.equals(valueA, testRow.valueA) && Objects.equals(valueB, testRow.valueB);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueA, valueB);
        }

        @Override
        public String toString() {
            return "TestRow{" +
                    "valueA='" + valueA + '\'' +
                    ", valueB='" + valueB + '\'' +
                    '}';
        }
    }
}
