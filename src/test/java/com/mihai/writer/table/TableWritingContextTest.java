package com.mihai.writer.table;

import com.mihai.core.workbook.Bounds;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableWritingContextTest {

    @Test
    public void appendedTableItLastTable() {
        TableWritingContext tableWritingContext = new TableWritingContext();
        WrittenTable table = new WrittenTable("id", new WrittenTableHeaders(1, List.of()), new Bounds(1, 1, 1, 1));
        tableWritingContext.appendTable(table);
        assertEquals(table, tableWritingContext.getLastWrittenTable());
    }

//    @Test
//    public void currentRowIsNegativeIfNotInWritingState() {
//        TableWritingContext tableWritingContext = new TableWritingContext();
//        assertEquals(-1, tableWritingContext.getCurrentRow());
//    }
//
//    @Test
//    public void currentColumnIsNegativeIfNotInWritingState() {
//        TableWritingContext tableWritingContext = new TableWritingContext();
//        assertEquals(-1, tableWritingContext.getCurrentColumn());
//    }

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

//    @Test
//    public void currentRowIsSaved() {
//        TableWritingContext tableWritingContext = new TableWritingContext();
//        tableWritingContext.setWritingTable(true);
//        tableWritingContext.setCurrentRow(42);
//
//        assertEquals(42, tableWritingContext.getCurrentRow());
//    }
//
//    @Test
//    public void currentColumnIsSaved() {
//        TableWritingContext tableWritingContext = new TableWritingContext();
//        tableWritingContext.setWritingTable(true);
//        tableWritingContext.setCurrentColumn(42);
//
//        assertEquals(42, tableWritingContext.getCurrentColumn());
//    }

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

//    @Test
//    public void currentColumnNameCorrespondsToCurrentColumn() {
//        TableWritingContext tableWritingContext = new TableWritingContext();
//
//        WrittenTableHeaders headers = new WrittenTableHeaders(1, List.of(
//                new WrittenTableHeader("A", 1), new WrittenTableHeader("B", 2))
//        );
//        tableWritingContext.setCurrentTableHeaders(headers);
//        tableWritingContext.setCurrentColumn(2);
//        tableWritingContext.setWritingTable(true);
//
//        assertEquals("B", tableWritingContext.getCurrentColumnName());
//    }
//
//    @Test
//    public void currentColumnNameEmptyIfNotInWritingState() {
//        TableWritingContext tableWritingContext = new TableWritingContext();
//
//        WrittenTableHeaders headers = new WrittenTableHeaders(1, List.of(
//                new WrittenTableHeader("A", 1), new WrittenTableHeader("B", 2))
//        );
//        tableWritingContext.setCurrentTableHeaders(headers);
//        tableWritingContext.setCurrentColumn(2);
//
//        assertEquals("", tableWritingContext.getCurrentColumnName());
//    }
}
