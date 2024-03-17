package com.mihai.writer.locator;

import org.apache.poi.ss.util.CellReference;

import java.util.Objects;

public class CellLocation {

    private final int row;
    private final int column;

    public CellLocation(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static CellLocation fromReference(String cellReference) {
        CellReference reference = new CellReference(cellReference);
        return new CellLocation(reference.getRow(), reference.getCol());
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CellLocation that = (CellLocation) object;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
