package com.mihai.writer.writers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.mihai.core.annotation.ExcelColumn;
import com.mihai.core.workbook.Bounds;
import com.mihai.core.workbook.CellLocation;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.ExcelWritingTest;
import com.mihai.writer.table.WrittenTable;
import org.junit.jupiter.api.Test;

public class TableWriterTest extends ExcelWritingTest {

    @Test
    public void tableFixedHeadersAndRowsAreWritten() {
        TableWriter writer = createTableWriter();

        List<FixedColumnsRow> rows = List.of(
                new FixedColumnsRow("row 1 value A", "row 1 value B"),
                new FixedColumnsRow("row 2 value A", "row 2 value B")
        );

        WrittenTable table = writer.writeTable(rows, FixedColumnsRow.class, "");

        assertEquals("", table.getId());
        assertEquals("test column A", table.getColumnName(0));
        assertEquals("test column B", table.getColumnName(1));
        assertEquals(new Bounds(0, 0, 2, 1), table.getBounds());

        assertEquals("test column A", getCell(0, 0).getStringCellValue());
        assertEquals("test column B", getCell(0, 1).getStringCellValue());
        assertEquals("row 1 value A", getCell(1, 0).getStringCellValue());
        assertEquals("row 1 value B", getCell(1, 1).getStringCellValue());
        assertEquals("row 2 value A", getCell(2, 0).getStringCellValue());
        assertEquals("row 2 value B", getCell(2, 1).getStringCellValue());
    }

    @Test
    public void onlyFixedHeadersWrittenWhenNoRowsToWrite() {
        TableWriter writer = createTableWriter();

        WrittenTable table = writer.writeTable(List.of(), FixedColumnsRow.class, "");

        assertEquals(new Bounds(0, 0, 0, 1), table.getBounds());

        assertEquals("test column A", getCell(0, 0).getStringCellValue());
        assertEquals("test column B", getCell(0, 1).getStringCellValue());
    }

    @Test
    public void tableOffsetIsApplied() {
        TableWriter writer = new TableWriter(getWritableSheet(),
                                             createSheetContext(),
                                             ExcelWritingSettings.builder()
                                                     .tableStartCellLocator((context, tableId) -> new CellLocation(3, 3)).build());

        List<FixedColumnsRow> rows = List.of(
                new FixedColumnsRow("", ""),
                new FixedColumnsRow("", "")
        );

        WrittenTable table = writer.writeTable(rows, FixedColumnsRow.class, "");
        assertEquals(new Bounds(3, 3, 5, 4), table.getBounds());
    }

    private static class FixedColumnsRow {

        @ExcelColumn(value = "test column A")
        private String columnA;

        @ExcelColumn(value = "test column B")
        private String columnB;

        private FixedColumnsRow(String columnA, String columnB) {
            this.columnA = columnA;
            this.columnB = columnB;
        }
    }
}
