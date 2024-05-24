package com.mihai.reader.readers;

import com.mihai.common.annotation.DynamicColumns;
import com.mihai.common.annotation.ExcelColumn;
import com.mihai.common.annotation.TableId;
import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.deserializer.DefaultDeserializationContext;
import com.mihai.reader.table.ReadTable;
import com.mihai.reader.table.TableHeaders;
import com.mihai.reader.workbook.sheet.Bounds;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TableReaderTest {

    private static final List<TestRow> EXPECTED_ROWS = List.of(
            new TestRow("value A", "value B"),
            new TestRow("value C", "value D")
    );

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
    public void invalidTypeParameterInDynamicColumnThrowsException() {
        actualSheet.createRow(0).createCell(0).setCellValue("A");
        actualSheet.createRow(1).createCell(0).setCellValue("B");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, new DefaultDeserializationContext(), null);
        TableReader tableReader = new TableReader(sheetContext, ExcelReadingSettings.DEFAULT);

        assertThrows(IllegalStateException.class, () -> tableReader.readRows(InvalidDynamicColumnType.class));
    }

    @Test
    public void readSimpleTable() {
        Row row1 = actualSheet.createRow(0);
        row1.createCell(0).setCellValue("Column A");
        row1.createCell(1).setCellValue("Column B");
        Row row2 = actualSheet.createRow(1);
        row2.createCell(0).setCellValue("value A");
        row2.createCell(1).setCellValue("value B");
        Row row3 = actualSheet.createRow(2);
        row3.createCell(0).setCellValue("value C");
        row3.createCell(1).setCellValue("value D");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, new DefaultDeserializationContext(), null);
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
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
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

    public static class GenericDynamicColumnType {

        @DynamicColumns
        private List<List<String>> value;
    }

    public static class InvalidDynamicColumnType {

        @DynamicColumns
        private Integer value;
    }
}
