package com.mihai.detector;

import com.mihai.workbook.PropertyCell;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class RowDetectors {

    private RowDetectors() {
        throw new IllegalStateException("Utility class");
    }

    public static RowDetector never() {
        return (context, rowCells) -> false;
    }

    public static RowDetector isFirstRow() {
        return isRowWithNumber(0);
    }

    public static RowDetector isRowWithNumber(int number) {
        return (context, rowCells) -> rowCells.getRowNumber() == number;
    }

    public static RowDetector isAbove(int rowNumber) {
        return (context, rowCells) -> rowCells.getRowNumber() > rowNumber;
    }

    public static RowDetector isBellow(int rowNumber) {
        return (context, rowCells) -> rowCells.getRowNumber() < rowNumber;
    }

    public static RowDetector hasAllValues(Set<String> values) {
        return (context, rowCells) -> {
            Set<String> rowValues = rowCells.stream()
                    .map(PropertyCell::getValue)
                    .collect(Collectors.toSet());
            return rowValues.containsAll(values);
        };
    }

    public static RowDetector allCellsEmpty() {
        return (context, rowCells) -> rowCells.stream()
                    .map(PropertyCell::getValue)
                    .allMatch(StringUtils::isEmpty);
    }
}
