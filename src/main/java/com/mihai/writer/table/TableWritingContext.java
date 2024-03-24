package com.mihai.writer.table;

public class TableWritingContext {

    private final WrittenTables writtenTables;

    private WrittenTableHeaders currentTableHeaders;

    private int currentRow;
    private int currentTableRow;
    private int currentColumn;
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

    public int getCurrentRow() {
        if(isWritingTable) {
            return currentRow;
        }
        return -1;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public int getCurrentTableRow() {
        if(isWritingTable) {
            return currentTableRow;
        }
        return -1;
    }

    public void setCurrentTableRow(int currentTableRow) {
        this.currentTableRow = currentTableRow;
    }

    public int getCurrentColumn() {
        if(isWritingTable) {
            return currentColumn;
        }
        return -1;
    }

    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
    }

    public int getCurrentTableColumn() {
        if(isWritingTable) {
            return currentTableColumn;
        }
        return -1;
    }

    public String getCurrentColumnName() {
        if(isWritingTable) {
            return currentTableHeaders.getColumnName(currentColumn);
        }
        return "";
    }

    public void setCurrentTableColumn(int currentTableColumn) {
        this.currentTableColumn = currentTableColumn;
    }

    public WrittenTableHeaders getCurrentTableHeaders() {
        if(isWritingTable) {
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
