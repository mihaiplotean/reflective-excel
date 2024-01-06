package com.mihai.workbook;

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

    @Override
    public Iterator<PropertyCell> iterator() {
        return cells.iterator();
    }

    public Stream<PropertyCell> stream() {
        return cells.stream();
    }
}
