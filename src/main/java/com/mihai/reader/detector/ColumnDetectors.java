package com.mihai.reader.detector;

import org.apache.commons.lang3.StringUtils;

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

    public static ColumnDetector cellValuesInOrder(String firstValue, String... followingValues) {
        return (context, columnCell) -> {
            if(!firstValue.equalsIgnoreCase(columnCell.getValue())) {
                return false;
            }
            int rowNumber = columnCell.getRowNumber();
            int columnNumber = columnCell.getColumnNumber();
            for(int i = 0; i < followingValues.length; i++) {
                String cellValue = context.getCellValue(rowNumber, columnNumber + i + 1);
                if(!followingValues[i].equalsIgnoreCase(cellValue)) {
                    return false;
                }
            }
            return true;
        };
    }

    public static ColumnDetector nextCellEmpty() {
        return (context, columnCell) -> {
            int rowNumber = columnCell.getRowNumber();
            int columnNumber = columnCell.getColumnNumber();
            return StringUtils.isEmpty(context.getCellValue(rowNumber, columnNumber + 1));
        };
    }
}
