package com.mihai.core.workbook;

import org.apache.poi.ss.util.CellReference;

public record CellLocation(int row, int column) {

    public CellLocation {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("The row or column of a cell cannot be negative");
        }
    }

    public static CellLocation fromReference(String cellReference) {
        CellReference reference = new CellReference(cellReference);
        int row = reference.getRow();
        int column = reference.getCol();
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Invalid cell reference: %s".formatted(cellReference));
        }
        return new CellLocation(row, column);
    }

    public CellLocation getLeftBy(int columns) {
        return getOffsetBy(0, -columns);
    }

    public CellLocation getRightBy(int columns) {
        return getOffsetBy(0, columns);
    }

    public CellLocation getUpBy(int rows) {
        return getOffsetBy(-rows, 0);
    }

    public CellLocation getDownBy(int rows) {
        return getOffsetBy(rows, 0);
    }

    public CellLocation getOffsetBy(int rows, int columns) {
        return new CellLocation(row + rows, column + columns);
    }

    public String getReference() {
        return new CellReference(row, column).formatAsString();
    }

    @Override
    public String toString() {
        return "CellLocation{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
