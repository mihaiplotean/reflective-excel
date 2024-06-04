package com.mihai.writer.table;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import com.mihai.core.workbook.Bounds;
import org.junit.jupiter.api.Test;

public class TableWritingContextTest {

    @Test
    public void appendedTableItLastTable() {
        TableWritingContext tableWritingContext = new TableWritingContext();
        WrittenTable table = new WrittenTable("id", new WrittenTableHeaders(1, List.of()), new Bounds(1, 1, 1, 1));
        tableWritingContext.appendTable(table);
        assertEquals(table, tableWritingContext.getLastWrittenTable());
    }

    @Test
    public void currentTableRowIsNegativeIfNotInWritingState() {
        TableWritingContext tableWritingContext = new TableWritingContext();
        assertEquals(-1, tableWritingContext.getCurrentTableRow());
    }

    @Test
    public void currentTableColumnIsNegativeIfNotInWritingState() {
        TableWritingContext tableWritingContext = new TableWritingContext();
        assertEquals(-1, tableWritingContext.getCurrentTableColumn());
    }

    @Test
    public void currentTableRowIsSaved() {
        TableWritingContext tableWritingContext = new TableWritingContext();
        tableWritingContext.setWritingTable(true);
        tableWritingContext.setCurrentTableRow(42);

        assertEquals(42, tableWritingContext.getCurrentTableRow());
    }

    @Test
    public void currentTableColumnIsSaved() {
        TableWritingContext tableWritingContext = new TableWritingContext();
        tableWritingContext.setWritingTable(true);
        tableWritingContext.setCurrentTableColumn(42);

        assertEquals(42, tableWritingContext.getCurrentTableColumn());
    }

    @Test
    public void headersAreSaved() {
        TableWritingContext tableWritingContext = new TableWritingContext();

        tableWritingContext.setWritingTable(true);
        WrittenTableHeaders headers = new WrittenTableHeaders(1, List.of());
        tableWritingContext.setCurrentTableHeaders(headers);

        assertEquals(headers, tableWritingContext.getCurrentTableHeaders());
    }

    @Test
    public void headersAreNotReturnedIfNotInWritingState() {
        TableWritingContext tableWritingContext = new TableWritingContext();

        WrittenTableHeaders headers = new WrittenTableHeaders(1, List.of());
        tableWritingContext.setCurrentTableHeaders(headers);

        assertNull(tableWritingContext.getCurrentTableHeaders());
    }
}
