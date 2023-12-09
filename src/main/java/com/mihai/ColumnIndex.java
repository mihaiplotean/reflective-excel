package com.mihai;

public class ColumnIndex {

    private final int index;
    private final String columnName;

    public ColumnIndex(int index, String columnName) {
        this.index = index;
        this.columnName = columnName;
    }

    public int getIndex() {
        return index;
    }

    public String getColumnName() {
        return columnName;
    }
}
