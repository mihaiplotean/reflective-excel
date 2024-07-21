package com.mihai.writer.table;

public class TableWritingContext {

    private final WrittenTables writtenTables;

    private WrittenTableHeaders currentTableHeaders;

    private int currentTableRow;
    private int currentTableColumn;

    private boolean isWritingTable;

    public TableWritingContext() {
        this.writtenTables = new WrittenTables();
    }

    public void appendTable(WrittenTable table) {
        writtenTables.append(table);
    }

    public WrittenTable getLastWrittenTable() {
        return writtenTables.getLastTable();
    }

    public WrittenTable getTable(String tableId) {
        return writtenTables.getTable(tableId);
    }

    public int getCurrentTableRow() {
        if (isWritingTable) {
            return currentTableRow;
        }
        return -1;
    }

    public void setCurrentTableRow(int currentTableRow) {
        this.currentTableRow = currentTableRow;
    }

    public int getCurrentTableColumn() {
        if (isWritingTable) {
            return currentTableColumn;
        }
        return -1;
    }

    public void setCurrentTableColumn(int currentTableColumn) {
        this.currentTableColumn = currentTableColumn;
    }

    public WrittenTableHeaders getCurrentTableHeaders() {
        if (isWritingTable) {
            return currentTableHeaders;
        }
        return null;
    }

    public void setCurrentTableHeaders(WrittenTableHeaders currentTableHeaders) {
        this.currentTableHeaders = currentTableHeaders;
    }

    public boolean isWritingTable() {
        return isWritingTable;
    }

    public void setWritingTable(boolean writingTable) {
        isWritingTable = writingTable;
    }
}
