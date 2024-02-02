package com.mihai;

import com.mihai.workbook.sheet.PropertyCell;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TableHeaders implements Iterable<TableHeader> {

    private final List<TableHeader> headers;
    private final Map<Integer, PropertyCell> columnIndexToHeaderCellsMap;

    public TableHeaders(List<TableHeader> headers) {
        this.headers = headers;
        this.columnIndexToHeaderCellsMap = headers.stream()
                .map(TableHeader::getCell)
                .collect(Collectors.toMap(PropertyCell::getColumnNumber, cell -> cell, (a, b) -> a));
    }

    public List<PropertyCell> getCells() {
        return List.copyOf(columnIndexToHeaderCellsMap.values());
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

    @Override
    public Iterator<TableHeader> iterator() {
        return headers.iterator();
    }
}
