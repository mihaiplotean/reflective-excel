package com.mihai;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class RowCells implements Iterable<ExcelCell> {

    private final List<ExcelCell> cells;

    public RowCells(List<ExcelCell> cells) {
        this.cells = cells;
    }

    public boolean allEmpty() {
        return cells.stream()
                .map(ExcelCell::getValue)
                .allMatch(StringUtils::isEmpty);
    }

    @Override
    public Iterator<ExcelCell> iterator() {
        return cells.iterator();
    }

    public Stream<ExcelCell> stream() {
        return cells.stream();
    }
}
