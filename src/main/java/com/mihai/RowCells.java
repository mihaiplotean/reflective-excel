package com.mihai;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class RowCells implements Iterable<PropertyCell> {

    private final Row row;
    private final List<PropertyCell> cells;

    public RowCells(Row row, List<PropertyCell> cells) {
        this.row = row;
        this.cells = cells;
    }

    public List<PropertyCell> getCells() {
        return cells;
    }

    public int getRowNumber() {
        return row.getRowNum();
    }

    public boolean allEmpty() {
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
