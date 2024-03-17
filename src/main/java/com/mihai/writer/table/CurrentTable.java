package com.mihai.writer.table;

public class CurrentTable {

    private final String id;
    private final WrittenTableHeaders headers;  // todo: rename this, use common one with the excel reader?

    private int currentRow;
    private int currentColumn;

    public CurrentTable(String id, WrittenTableHeaders headers) {
        this.id = id;
        this.headers = headers;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
    }
}
