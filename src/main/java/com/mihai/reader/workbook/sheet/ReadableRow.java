package com.mihai.reader.workbook.sheet;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents a row, or a part of a row in the Excel sheet. Since, for simplicity, this framework considers merged cells
 * as one single cell, the row might contain cells that start in another row but intersect this one.
 */
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
                .filter(cell -> cell.isWithinColumnBounds(columnIndex))
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ReadableRow other = (ReadableRow) object;
        return row == other.row && Objects.equals(cells, other.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, cells);
    }
}
