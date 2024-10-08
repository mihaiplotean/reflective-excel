package com.reflectiveexcel.writer.writers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import com.reflectiveexcel.core.annotation.DynamicColumns;
import com.reflectiveexcel.core.annotation.ExcelColumn;
import com.reflectiveexcel.writer.ExcelWritingTest;
import com.reflectiveexcel.writer.WritableSheetContext;
import com.reflectiveexcel.writer.node.RootTableBeanWriteNode;
import org.junit.jupiter.api.Test;

public class RowWriterTest extends ExcelWritingTest {

    @Test
    public void rowsWithFixedColumnsWritten() {
        FixedColumnsRow row1 = new FixedColumnsRow("value A", "value B");
        FixedColumnsRow row2 = new FixedColumnsRow("value C", "value D");
        RowWriter writer = createWriter(row1);

        writer.writeRow(row1);
        writer.writeRow(row2);

        // row 0 is reserved for the header, so the first written row has index 1
        assertEquals("value A", getCell(1, 0).getStringCellValue());
        assertEquals("value B", getCell(1, 1).getStringCellValue());
        assertEquals("value C", getCell(2, 0).getStringCellValue());
        assertEquals("value D", getCell(2, 1).getStringCellValue());
    }

    @Test
    public void onlyValuesCorrespondingToColumnsOfFirstRowAreWritten() {
        DynamicColumnsRow row1 = new DynamicColumnsRow(Map.of("col 1", "a"));
        DynamicColumnsRow row2 = new DynamicColumnsRow(Map.of("col 1", "b", "col 2", "c"));
        RowWriter writer = createWriter(row1);

        writer.writeRow(row1);
        writer.writeRow(row2);

        assertEquals("a", getCell(1, 0).getStringCellValue());
        assertNull(getCell(1, 1));
        assertEquals("b", getCell(2, 0).getStringCellValue());
        assertNull(getCell(2, 1));
    }

    @Test
    public void tableRowIndexUpdatedAfterRowWritten() {
        DynamicColumnsRow row1 = new DynamicColumnsRow(Map.of("col 1", "a"));

        RootTableBeanWriteNode node = new RootTableBeanWriteNode(DynamicColumnsRow.class, row1);
        WritableSheetContext sheetContext = createSheetContext();
        sheetContext.setWritingTable(true);
        RowWriter rowWriter = new RowWriter(sheetContext, node, new CellWriter(getWritableSheet()));

        assertEquals(0, sheetContext.getWritingContext().getCurrentTableRow());
        rowWriter.writeRow(row1);
        assertEquals(1, sheetContext.getWritingContext().getCurrentTableRow());
    }

    private RowWriter createWriter(Object firstRow) {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(firstRow.getClass(), firstRow);
        return new RowWriter(createSheetContext(), node, new CellWriter(getWritableSheet()));
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

    private static class DynamicColumnsRow {

        @DynamicColumns
        private Map<String, String> valuePerColumn;

        private DynamicColumnsRow(Map<String, String> valuePerColumn) {
            this.valuePerColumn = valuePerColumn;
        }
    }
}
