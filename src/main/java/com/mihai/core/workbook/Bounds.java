package com.mihai.core.workbook;

public record Bounds(int startRow, int startColumn, int endRow, int endColumn) {

    public Bounds {
        validateRowBounds(startRow, endRow);
        validateColumnBounds(startColumn, endColumn);
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
