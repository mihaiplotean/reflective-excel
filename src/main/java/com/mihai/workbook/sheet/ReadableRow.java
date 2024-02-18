package com.mihai.workbook.sheet;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ReadableRow implements Iterable<ReadableCell> {

    private final int row;
    private final List<ReadableCell> cells;

    public ReadableRow(int row, List<ReadableCell> cells) {
        this.row = row;
        this.cells = List.copyOf(cells);
    }

    public List<ReadableCell> getCells() {
        return cells;
    }

    public ReadableCell getCell(int columnIndex) {
        return cells.stream()
                .filter(cell -> cell.getColumnNumber() == columnIndex)
                .findFirst()
                .orElse(null);
    }

    public int getRowNumber() {
        return row;
    }

    public boolean isEmpty() {
        return cells.stream()
                .map(ReadableCell::getValue)
                .allMatch(StringUtils::isEmpty);
    }

    @Override
    public Iterator<ReadableCell> iterator() {
        return cells.iterator();
    }

    public Stream<ReadableCell> stream() {
        return cells.stream();
    }
}
