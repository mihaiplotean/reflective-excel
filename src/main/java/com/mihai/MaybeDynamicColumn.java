package com.mihai;

import java.util.List;

public class MaybeDynamicColumn {

    private final List<ColumnIndex> columnIndices;
    private final ColumnIndex currentColumn;

    public MaybeDynamicColumn(List<ColumnIndex> columnIndices, ColumnIndex currentColumn) {
        this.columnIndices = columnIndices;
        this.currentColumn = currentColumn;
    }

    public String getName() {
        return currentColumn.getColumnName();
    }

    public int getIndex() {
        return currentColumn.getIndex();
    }

    public boolean isAfter(String otherColumn) {
        int otherColumnIndex = indexOf(otherColumn);
        if(otherColumnIndex < 0) {
            return false;
        }
        return getIndex() > otherColumnIndex;
    }

    public boolean isBefore(String otherColumn) {
        int otherColumnIndex = indexOf(otherColumn);
        if(otherColumnIndex < 0) {
            return false;
        }
        return getIndex() < indexOf(otherColumn);
    }

    public boolean isBetween(String columnA, String columnB) {
        return isAfter(columnA) && isBefore(columnB);
    }

    public boolean equals(String other) {
        return getName().equalsIgnoreCase(other);
    }

    public boolean matchesPattern(String pattern) {
        return getName().matches(pattern);
    }

    private int indexOf(String columnName) {
        for (ColumnIndex columnIndex : columnIndices) {
            if(columnName.equalsIgnoreCase(columnIndex.getColumnName())) {
                return columnIndex.getIndex();
            }
        }
        return -1;
    }
}
