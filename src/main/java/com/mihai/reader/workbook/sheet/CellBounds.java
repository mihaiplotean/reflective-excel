package com.mihai.reader.workbook.sheet;

import org.apache.poi.ss.usermodel.Cell;

import java.util.Objects;

public final class CellBounds {

    private final Cell valueCell;
    private final Bounds bounds;

    public CellBounds(Cell cell) {
        this(cell, cell.getRowIndex(), cell.getColumnIndex(), cell.getRowIndex(), cell.getColumnIndex());
    }

    public CellBounds(Cell valueCell, int startRow, int startColumn, int endRow, int endColumn) {
        this.valueCell = valueCell;
        this.bounds = new Bounds(startRow, startColumn, endRow, endColumn);
    }

    public Cell valueCell() {
        return valueCell;
    }

    public int startRow() {
        return bounds.getStartRow();
    }

    public int startColumn() {
        return bounds.getStartColumn();
    }

    public int endRow() {
        return bounds.getEndRow();
    }

    public int endColumn() {
        return bounds.getEndColumn();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellBounds that = (CellBounds) o;
        return Objects.equals(valueCell, that.valueCell)
                && Objects.equals(bounds, that.bounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueCell, bounds);
    }
}
