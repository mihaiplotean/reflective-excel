package com.mihai.detector;

public class ColumnDetectors {

    private ColumnDetectors() {
        throw new IllegalStateException("Utility class");
    }

    public static ColumnDetector never() {
        return (context, columnCell) -> false;
    }

    public static ColumnDetector isFirstColumn() {
        return isColumnWithNumber(0);
    }

    public static ColumnDetector isColumnWithNumber(int columnNumber) {
        return (context, columnCell) -> columnCell.getColumnNumber() == columnNumber;
    }

    public static ColumnDetector isAfter(int columnNumber) {
        return (context, columnCell) -> columnCell.getColumnNumber() > columnNumber;
    }

    public static ColumnDetector isBefore(int columnNumber) {
        return (context, columnCell) -> columnCell.getColumnNumber() < columnNumber;
    }
}
