package com.mihai.reader.table;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.mihai.core.annotation.TableId;
import com.mihai.reader.bean.RootTableBeanReadNode;
import com.mihai.reader.workbook.sheet.ReadableRow;
import org.junit.jupiter.api.Test;

class TableReadingContextTest {

    @Test
    public void tableIdIsReturnedWhenInReadingState() {
        TableReadingContext tableReadingContext = new TableReadingContext();
        tableReadingContext.setCurrentBeanNode(new RootTableBeanReadNode(DummyTable.class));
        tableReadingContext.setReadingTable(true);
        assertEquals("dummy table", tableReadingContext.getCurrentTableId());
    }

    @Test
    public void tableIdIsEmptyWhenNotInReadingState() {
        TableReadingContext tableReadingContext = new TableReadingContext();
        tableReadingContext.setReadingTable(false);
        assertEquals("", tableReadingContext.getCurrentTableId());
    }

    @Test
    public void currentRowReturnedWhenInReadingState() {
        TableReadingContext tableReadingContext = new TableReadingContext();
        tableReadingContext.setReadingTable(true);
        tableReadingContext.setCurrentTableRow(42);
        assertEquals(42, tableReadingContext.getCurrentTableRow());
    }

    @Test
    public void currentRowNegativeWhenNotInReadingState() {
        TableReadingContext tableReadingContext = new TableReadingContext();
        tableReadingContext.setReadingTable(false);
        tableReadingContext.setCurrentTableRow(42);
        assertTrue(tableReadingContext.getCurrentTableRow() < 0);
    }

    @Test
    public void currentColumnReturnedWhenInReadingState() {
        TableReadingContext tableReadingContext = new TableReadingContext();
        tableReadingContext.setReadingTable(true);
        tableReadingContext.setCurrentTableColumn(42);
        assertEquals(42, tableReadingContext.getCurrentTableColumn());
    }

    @Test
    public void currentColumnNegativeWhenNotInReadingState() {
        TableReadingContext tableReadingContext = new TableReadingContext();
        tableReadingContext.setReadingTable(false);
        assertTrue(tableReadingContext.getCurrentTableColumn() < 0);
    }

    @Test
    public void currentTableHeadersReturnedWhenInReadingState() {
        TableReadingContext tableReadingContext = new TableReadingContext();
        TableHeaders tableHeaders = new TableHeaders(List.of());
        tableReadingContext.setCurrentTableHeaders(tableHeaders);
        tableReadingContext.setReadingTable(true);

        assertEquals(tableHeaders, tableReadingContext.getCurrentTableHeaders());
    }

    @Test
    public void currentTableHeadersAreNullWhenNotInReadingState() {
        TableReadingContext tableReadingContext = new TableReadingContext();
        TableHeaders tableHeaders = new TableHeaders(List.of());
        tableReadingContext.setCurrentTableHeaders(tableHeaders);
        tableReadingContext.setReadingTable(false);

        assertNull(tableReadingContext.getCurrentTableHeaders());
    }

    @Test
    public void boundingNullRowToCurrentTableReturnsNull() {
        TableReadingContext tableReadingContext = new TableReadingContext();
        assertNull(tableReadingContext.boundToCurrentTable(null));
    }

    @Test
    public void boundingRowToCurrentTableWithNullHeadersReturnsEmptyRow() {
        TableReadingContext tableReadingContext = new TableReadingContext();
        ReadableRow row = new ReadableRow(1, List.of(new DummyCell()));
        tableReadingContext.setCurrentTableHeaders(null);

        assertEquals(new ReadableRow(1, List.of()), tableReadingContext.boundToCurrentTable(row));
    }

    @TableId("dummy table")
    private static class DummyTable {

    }
}
