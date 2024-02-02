package com.mihai.workbook.sheet;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class RowCells implements Iterable<PropertyCell> {

    private final int row;
    private final List<PropertyCell> cells;

    public RowCells(int row, List<PropertyCell> cells) {
        this.row = row;
        this.cells = List.copyOf(cells);
    }

    public List<PropertyCell> getCells() {
        return cells;
    }

    public PropertyCell getCell(int columnIndex) {
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
                .map(PropertyCell::getValue)
                .allMatch(StringUtils::isEmpty);
    }

    @Override
    public Iterator<PropertyCell> iterator() {
        return cells.iterator();
    }

    public Stream<PropertyCell> stream() {
        return cells.stream();
    }
}
