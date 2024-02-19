package com.mihai.reader.workbook.sheet;

import org.apache.poi.ss.usermodel.Cell;

import java.util.Objects;

public final class CellBounds {

    private final Cell valueCell;
    private final int startRow;
    private final int startColumn;
    private final int endRow;
    private final int endColumn;

    public CellBounds(Cell cell) {
        this(cell, cell.getRowIndex(), cell.getColumnIndex(), cell.getRowIndex(), cell.getColumnIndex());
    }

    public CellBounds(Cell valueCell, int startRow, int startColumn, int endRow, int endColumn) {
        this.valueCell = valueCell;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
    }

    public boolean inBounds(int row, int column) {
        return rowInBounds(row) && columnInBounds(column);
    }

    private boolean rowInBounds(int row) {
        return startRow <= row && row <= endRow;
    }

    private boolean columnInBounds(int column) {
        return startColumn <= column && column <= endColumn;
    }

    public Cell valueCell() {
        return valueCell;
    }

    public int startRow() {
        return startRow;
    }

    public int startColumn() {
        return startColumn;
    }

    public int endRow() {
        return endRow;
    }

    public int endColumn() {
        return endColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellBounds that = (CellBounds) o;
        return startRow == that.startRow
                && startColumn == that.startColumn
                && endRow == that.endRow
                && endColumn == that.endColumn
                && Objects.equals(valueCell, that.valueCell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueCell, startRow, startColumn, endRow, endColumn);
    }
}
