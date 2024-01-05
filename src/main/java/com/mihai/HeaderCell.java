package com.mihai;

import org.apache.poi.ss.usermodel.Cell;

public class HeaderCell extends PropertyCell {

    private enum ColumnType {
        FIXED,
        DYNAMIC
    }

    private final ColumnType columnType;

    private HeaderCell(Cell cell, String cellValue, ColumnType columnType) {
        super(cell, cellValue);

        this.columnType = columnType;
    }

    private HeaderCell dynamic(Cell cell, String cellValue) {
        return new HeaderCell(cell, cellValue, ColumnType.DYNAMIC);
    }

    private HeaderCell fixed(Cell cell, String cellValue) {
        return new HeaderCell(cell, cellValue, ColumnType.FIXED);
    }

    public boolean isDynamic() {
        return columnType == ColumnType.DYNAMIC;
    }

    public boolean isFixed() {
        return columnType == ColumnType.FIXED;
    }
}
