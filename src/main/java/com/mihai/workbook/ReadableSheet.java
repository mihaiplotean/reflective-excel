package com.mihai.workbook;

import com.mihai.CellValueFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ReadableSheet implements Iterable<RowCells> {

    private final Sheet sheet;
    private final CellValueFormatter cellValueFormatter;

    public ReadableSheet(Sheet sheet) {
        this.sheet = sheet;
        this.cellValueFormatter = new CellValueFormatter(sheet.getWorkbook());  // todo: make overridable
    }

    public PropertyCell getCell(String cellReference) {
        CellReference reference = new CellReference(cellReference);
        return getCell(reference.getRow(), reference.getCol());
    }

    public PropertyCell getCell(int rowIndex, int columnIndex) {
        return Optional.ofNullable(sheet.getRow(rowIndex))
                .map(row -> row.getCell(columnIndex))
                .map(this::asPropertyCell)
                .orElse(null);
    }

    public PropertyCell asPropertyCell(Cell cell) {
        return new PropertyCell(cell, cellValueFormatter.toString(cell));
    }

    @Override
    public Iterator<RowCells> iterator() {
        Iterator<Row> iterator = sheet.iterator();

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public RowCells next() {
                Row row = iterator.next();
                List<PropertyCell> cells = new ArrayList<>();
                for (Cell cell : row) {
                    cells.add(asPropertyCell(cell));
                }
                return new RowCells(row, cells);
            }
        };
    }
}
