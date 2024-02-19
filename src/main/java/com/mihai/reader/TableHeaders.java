package com.mihai.reader;

import com.mihai.reader.workbook.sheet.ReadableCell;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableHeaders implements Iterable<TableHeader> {

    private final List<TableHeader> headers;
    private final Map<Integer, ReadableCell> columnIndexToHeaderCellsMap;

    public TableHeaders(List<TableHeader> headers) {
        this.headers = headers;
        this.columnIndexToHeaderCellsMap = headers.stream()
                .map(TableHeader::getCell)
                .collect(Collectors.toMap(ReadableCell::getColumnNumber, cell -> cell, (a, b) -> a));
    }

    public List<ReadableCell> getCells() {
        return List.copyOf(columnIndexToHeaderCellsMap.values());
    }

    public ReadableCell getHeader(int columnIndex) {
        return columnIndexToHeaderCellsMap.get(columnIndex);
    }

    public ReadableCell getHeader(String headerName) {
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
