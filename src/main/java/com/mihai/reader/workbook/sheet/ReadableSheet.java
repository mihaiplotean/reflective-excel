package com.mihai.reader.workbook.sheet;

import com.mihai.reader.CellValueFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import java.util.*;

public class ReadableSheet implements Iterable<ReadableRow> {

    private final Sheet sheet;
    private final MergedRegionValues mergedRegionValues;
    private final CellValueFormatter cellValueFormatter;

    public ReadableSheet(Sheet sheet) {
        this.sheet = sheet;
        this.mergedRegionValues = new MergedRegionValues(sheet);
        this.cellValueFormatter = new CellValueFormatter(sheet.getWorkbook());  // todo: make overridable
    }

    public ReadableRow getRow(int rowNumber) {
        Row actualRow = sheet.getRow(rowNumber);
        if(actualRow == null) {
            return new ReadableRow(rowNumber, Collections.emptyList());
        }
        List<ReadableCell> cells = new ArrayList<>();
        for (Cell cell : actualRow) {
            cells.add(asPropertyCell(cell));
        }
        return new ReadableRow(rowNumber, cells);
    }

    public ReadableCell getCell(String cellReference) {
        CellReference reference = new CellReference(cellReference);
        return getCell(reference.getRow(), reference.getCol());
    }

    public ReadableCell getCell(int rowIndex, int columnIndex) {
        return Optional.ofNullable(sheet.getRow(rowIndex))
                .map(row -> row.getCell(columnIndex))
                .map(this::asPropertyCell)
                .orElse(null);
    }

    public CellBounds getCellBounds(int rowIndex, int columnIndex) {
        return Optional.ofNullable(sheet.getRow(rowIndex))
                .map(row -> row.getCell(columnIndex))
                .map(this::getCellBounds)
                .orElse(null);
    }

    public CellBounds getCellBounds(Cell cell) {
        return mergedRegionValues.getCellBounds(cell);
    }

    public ReadableCell asPropertyCell(Cell cell) {
        CellBounds cellBounds = getCellBounds(cell);
        return new ReadableCell(cell, cellBounds, cellValueFormatter.toString(cellBounds.valueCell()));
    }

    @Override
    public Iterator<ReadableRow> iterator() {
        Iterator<Row> iterator = sheet.iterator();

        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }


            @Override
            public ReadableRow next() {
                Row row = iterator.next();
                List<ReadableCell> cells = new ArrayList<>();
                for (Cell cell : row) {
                    cells.add(asPropertyCell(cell));
                }
                return new ReadableRow(row.getRowNum(), cells);
            }
        };
    }
}
