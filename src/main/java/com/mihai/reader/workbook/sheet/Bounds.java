package com.mihai.reader.workbook.sheet;

import com.mihai.writer.locator.CellLocation;

import java.util.Objects;

public class Bounds {

    private final int startRow;
    private final int startColumn;
    private final int endRow;
    private final int endColumn;

    public Bounds(int startRow, int startColumn, int endRow, int endColumn) {
        validateRowBounds(startRow, endRow);
        validateColumnBounds(startColumn, endColumn);

        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
    }

    private static void validateRowBounds(int startRow, int endRow) {
        if (endRow - startRow < 0) {
            throw new IllegalArgumentException("End row should be after start row");
        }
    }

    private static void validateColumnBounds(int startColumn, int endColumn) {
        if (endColumn - startColumn < 0) {
            throw new IllegalArgumentException("End column should be after start column");
        }
    }

    public CellLocation getTopLeftLocation() {
        return new CellLocation(startRow, startColumn);
    }

    public CellLocation getTopRightLocation() {
        return new CellLocation(startRow, endColumn);
    }

    public CellLocation getBottomLeftLocation() {
        return new CellLocation(endRow, startColumn);
    }

    public CellLocation getBottomRightLocation() {
        return new CellLocation(endRow, endColumn);
    }

    public int getHeight() {
        return endRow - startRow + 1;
    }

    public int getLength() {
        return endColumn - startColumn + 1;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndColumn() {
        return endColumn;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Bounds bounds = (Bounds) object;
        return startRow == bounds.startRow
                && startColumn == bounds.startColumn
                && endRow == bounds.endRow
                && endColumn == bounds.endColumn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startRow, startColumn, endRow, endColumn);
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "startRow=" + startRow +
                ", startColumn=" + startColumn +
                ", endRow=" + endRow +
                ", endColumn=" + endColumn +
                '}';
    }
}
