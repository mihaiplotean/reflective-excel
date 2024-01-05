package com.mihai;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableHeaders {

    private Map<Integer, PropertyCell> columnIndexToHeaderCellsMap;

    public TableHeaders(List<PropertyCell> headerCells) {
        this.columnIndexToHeaderCellsMap = headerCells.stream()
                .collect(Collectors.toMap(PropertyCell::getColumnNumber, cell -> cell, (a, b) -> a));
    }

    public List<PropertyCell> getCells() {
        return List.copyOf(columnIndexToHeaderCellsMap.values());
    }

    public PropertyCell getHeader(int columnIndex) {
        return columnIndexToHeaderCellsMap.get(columnIndex);
    }
}
