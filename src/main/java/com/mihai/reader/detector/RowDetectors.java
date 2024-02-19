package com.mihai.reader.detector;

import com.mihai.reader.workbook.sheet.ReadableCell;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
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

    public static RowDetector hasAllValues(String... values) {
        return hasAllValues(Set.of(values));
    }

    public static RowDetector hasAllValues(Set<String> values) {
        return (context, rowCells) -> {
            Set<String> rowValues = rowCells.stream()
                    .map(ReadableCell::getValue)
                    .collect(toCaseInsensitiveSet());
            return rowValues.containsAll(values);
        };
    }

    public static RowDetector allCellsEmpty() {
        return (context, rowCells) -> rowCells.stream()
                .map(ReadableCell::getValue)
                .allMatch(StringUtils::isEmpty);
    }

    public static RowDetector nextRowEmpty() {
        return (context, rowCells) -> context.getRow(rowCells.getRowNumber() + 1).isEmpty();
    }

    private static Collector<String, ?, Set<String>> toCaseInsensitiveSet() {
        return Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
    }
}
