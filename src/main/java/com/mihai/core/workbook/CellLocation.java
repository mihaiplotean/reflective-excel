package com.mihai.core.workbook;

import org.apache.poi.ss.util.CellReference;

/**
 * Represents the coordinates of a cell in the Excel sheet.
 * The top-left cell has the row and column equal to zero.
 *
 * @param row    the row index in the Excel sheet.
 * @param column the column index in the Excel sheet.
 */
public record CellLocation(int row, int column) {

    public CellLocation {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("The row or column of a cell cannot be negative");
        }
    }

    /**
     * Constructs a cell location from a cell reference.
     * <pre>
     *     "A1" --> cell location with row = 0 and column = 0
     *     "B2" --> cell location with row = 1 and column = 1
     * </pre>
     */
    public static CellLocation fromReference(String cellReference) {
        CellReference reference = new CellReference(cellReference);
        int row = reference.getRow();
        int column = reference.getCol();
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Invalid cell reference: %s".formatted(cellReference));
        }
        return new CellLocation(row, column);
    }

    /**
     * Creates a new instance of this object, where the column is shifted to the left.
     *
     * @param columns the amount of columns to shift.
     * @return a new instance of this object, with the given number of columns to the left.
     */
    public CellLocation getLeftBy(int columns) {
        return getOffsetBy(0, -columns);
    }

    /**
     * Creates a new instance of this object, where the column is shifted to the right.
     *
     * @param columns the amount of columns to shift.
     * @return a new instance of this object, with the given number of columns to the right.
     */
    public CellLocation getRightBy(int columns) {
        return getOffsetBy(0, columns);
    }

    /**
     * Creates a new instance of this object, where the row is shifted up.
     *
     * @param rows the amount of rows to shift.
     * @return a new instance of this object, with the given number of rows up.
     */
    public CellLocation getUpBy(int rows) {
        return getOffsetBy(-rows, 0);
    }

    /**
     * Creates a new instance of this object, where the row is shifted down.
     *
     * @param rows the amount of rows to shift.
     * @return a new instance of this object, with the given number of rows down.
     */
    public CellLocation getDownBy(int rows) {
        return getOffsetBy(rows, 0);
    }

    /**
     * Creates a new instance of this object, where the row is shifted down, and the column to the right.
     *
     * @param rows    the amount of rows to shift.
     * @param columns the amount of columns to shift.
     * @return a new instance of this object, with the given number of rows down and the given number of columns
     * to the right.
     */
    public CellLocation getOffsetBy(int rows, int columns) {
        return new CellLocation(row + rows, column + columns);
    }

    /**
     * @return cell reference, corresponding to the location.
     */
    public String getReference() {
        return new CellReference(row, column).formatAsString();
    }
}
