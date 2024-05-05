package com.mihai.writer.writers;

import com.mihai.common.annotation.ExcelColumn;
import com.mihai.reader.workbook.sheet.Bounds;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.WritableSheetContext;
import com.mihai.writer.WritableSheet;
import com.mihai.writer.locator.CellLocation;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.style.DefaultStyleContext;
import com.mihai.writer.table.WrittenTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TableWriterTest {

    private XSSFWorkbook workbook;
    private XSSFSheet actualSheet;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        actualSheet = workbook.createSheet();
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void tableFixedHeadersAndRowsAreWritten() {
        TableWriter writer = new TableWriter(new WritableSheet(actualSheet),
                new WritableSheetContext(new DefaultSerializationContext(), new DefaultStyleContext()), ExcelWritingSettings.DEFAULT);

        List<FixedColumnsRow> rows = List.of(
                new FixedColumnsRow("row 1 value A", "row 1 value B"),
                new FixedColumnsRow("row 2 value A", "row 2 value B")
        );

        WrittenTable table = writer.writeTable(rows, FixedColumnsRow.class, "");

        assertEquals("", table.getId());
        assertEquals("test column A", table.getColumnName(0));
        assertEquals("test column B", table.getColumnName(1));
        assertEquals(new Bounds(0, 0, 2, 1), table.getBounds());

        assertEquals("test column A", actualSheet.getRow(0).getCell(0).getStringCellValue());
        assertEquals("test column B", actualSheet.getRow(0).getCell(1).getStringCellValue());
        assertEquals("row 1 value A", actualSheet.getRow(1).getCell(0).getStringCellValue());
        assertEquals("row 1 value B", actualSheet.getRow(1).getCell(1).getStringCellValue());
        assertEquals("row 2 value A", actualSheet.getRow(2).getCell(0).getStringCellValue());
        assertEquals("row 2 value B", actualSheet.getRow(2).getCell(1).getStringCellValue());
    }

    @Test
    public void onlyFixedHeadersWrittenWhenNoRowsToWrite() {
        TableWriter writer = new TableWriter(new WritableSheet(actualSheet),
                new WritableSheetContext(new DefaultSerializationContext(), new DefaultStyleContext()), ExcelWritingSettings.DEFAULT);

        WrittenTable table = writer.writeTable(List.of(), FixedColumnsRow.class, "");

        assertEquals(new Bounds(0, 0, 0, 1), table.getBounds());

        assertEquals("test column A", actualSheet.getRow(0).getCell(0).getStringCellValue());
        assertEquals("test column B", actualSheet.getRow(0).getCell(1).getStringCellValue());
    }

    @Test
    public void tableOffsetIsApplied() {
        TableWriter writer = new TableWriter(new WritableSheet(actualSheet),
                new WritableSheetContext(new DefaultSerializationContext(), new DefaultStyleContext()),
                ExcelWritingSettings.with().tableStartCellLocator((context, tableId) -> new CellLocation(3, 3)).create());

        List<FixedColumnsRow> rows = List.of(
                new FixedColumnsRow("", ""),
                new FixedColumnsRow("", "")
        );

        WrittenTable table = writer.writeTable(rows, FixedColumnsRow.class, "");
        assertEquals(new Bounds(3, 3, 5, 4), table.getBounds());
    }

    private static class FixedColumnsRow {

        @ExcelColumn(name = "test column A")
        private String columnA;

        @ExcelColumn(name = "test column B")
        private String columnB;

        private FixedColumnsRow(String columnA, String columnB) {
            this.columnA = columnA;
            this.columnB = columnB;
        }
    }
}