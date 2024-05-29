package com.mihai.reader.table;

import com.mihai.reader.bean.RootTableBeanReadNode;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;

import java.util.List;

public class TableReadingContext {

    private final ReadTables readTables;

    private RootTableBeanReadNode currentBeanNode;
    private TableHeaders currentTableHeaders;

    private int currentTableRow;
    private int currentTableColumn;

    private boolean isReadingTable;

    public TableReadingContext() {
        this.readTables = new ReadTables();
    }

    public RootTableBeanReadNode getCurrentBeanNode() {
        return currentBeanNode;
    }

    public void setCurrentBeanNode(RootTableBeanReadNode currentBeanNode) {
        this.currentBeanNode = currentBeanNode;
    }

    public void appendTable(ReadTable table) {
        readTables.append(table);
    }

    public ReadTable getLastReadTable() {
        return readTables.getLastTable();
    }

    public ReadTable getTable(String tableId) {
        return readTables.getTable(tableId);
    }

    public String getCurrentTableId() {
        if(isReadingTable) {
            return currentBeanNode.getTableId();
        }
        return "";
    }

    public int getCurrentTableRow() {
        if (isReadingTable) {
            return currentTableRow;
        }
        return -1;
    }

    public void setCurrentTableRow(int currentTableRow) {
        this.currentTableRow = currentTableRow;
    }

    public int getCurrentTableColumn() {
        if (isReadingTable) {
            return currentTableColumn;
        }
        return -1;
    }

    public void setCurrentTableColumn(int currentTableColumn) {
        this.currentTableColumn = currentTableColumn;
    }

    public TableHeaders getCurrentTableHeaders() {
        if (isReadingTable) {
            return currentTableHeaders;
        }
        return null;
    }

    public void setCurrentTableHeaders(TableHeaders currentTableHeaders) {
        this.currentTableHeaders = currentTableHeaders;
    }

    public boolean isReadingTable() {
        return isReadingTable;
    }

    public void setReadingTable(boolean isReadingTable) {
        this.isReadingTable = isReadingTable;
    }

    public ReadableRow boundToCurrentTable(ReadableRow row) {
        if (row == null) {
            return null;
        }
        if(getCurrentTableHeaders() == null) {
            return new ReadableRow(row.getRowNumber(), List.of());
        }
        List<ReadableCell> tableRowCells = row.stream()
                .filter(cell -> currentTableHeaders.getHeader(cell.getColumnNumber()) != null)
                .toList();
        return new ReadableRow(row.getRowNumber(), tableRowCells);
    }
}
