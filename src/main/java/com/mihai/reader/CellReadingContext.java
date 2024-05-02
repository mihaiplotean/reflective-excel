package com.mihai.reader;

// todo: this will be a copy of CellWritingContext?
public class CellReadingContext {

    private int currentRow = -1;
    private int currentColumn = -1;

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

    public void reset() {
        currentRow = -1;
        currentColumn = -1;
    }
}
