package com.mihai;

import com.mihai.workbook.PropertyCell;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableHeaders {

    private final int rowNumber;
    private final Map<Integer, PropertyCell> columnIndexToHeaderCellsMap;

    public TableHeaders(int rowNumber, List<PropertyCell> headerCells) {
        this.rowNumber = rowNumber;
        this.columnIndexToHeaderCellsMap = headerCells.stream()
                .collect(Collectors.toMap(PropertyCell::getColumnNumber, cell -> cell, (a, b) -> a));
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public List<PropertyCell> getCells() {
        return List.copyOf(columnIndexToHeaderCellsMap.values());
    }

    public boolean contains(PropertyCell cell) {
        return columnIndexToHeaderCellsMap.values().stream()
                .anyMatch(headerCell -> headerCell.equalLocation(cell));
    }

    public PropertyCell getHeader(int columnIndex) {
        return columnIndexToHeaderCellsMap.get(columnIndex);
    }

    public PropertyCell getHeader(String headerName) {
        return columnIndexToHeaderCellsMap.values().stream()
                .filter(headerCell -> headerCell.getValue().equalsIgnoreCase(headerName))
                .findFirst()
                .orElse(null);
    }
}
