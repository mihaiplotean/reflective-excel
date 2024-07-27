package com.reflectiveexcel.reader.workbook.sheet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.reflectiveexcel.reader.CellValueFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

public class ReadableSheet implements Iterable<ReadableRow> {

    private final Sheet sheet;
    private final MergedCellsFinder mergedCellsFinder;
    private final CellValueFormatter cellValueFormatter;
    private final Map<Integer, ReadableRow> rowNumberToRowMap = new HashMap<>();

    public ReadableSheet(Sheet sheet) {
        this.sheet = sheet;
        this.cellValueFormatter = new CellValueFormatter(sheet.getWorkbook());
        this.mergedCellsFinder = new MergedCellsFinder(sheet, cellValueFormatter);
    }

    public ReadableRow getRow(int rowNumber) {
        return rowNumberToRowMap.computeIfAbsent(rowNumber, this::createRow);
    }

    private ReadableRow createRow(int rowNumber) {
        Row actualRow = sheet.getRow(rowNumber);
        List<MergedCell> cellsIntersectingRow = mergedCellsFinder.getMergedCellsIntersectingRow(rowNumber);
        if (actualRow == null) {
            return new ReadableRow(rowNumber, sortedByColumn(cellsIntersectingRow));
        }
        List<SimpleCell> cells = getSimpleCells(actualRow);
        List<SimpleCell> nonIntersectingCells = removeIntersectingCells(cells, cellsIntersectingRow);
        List<ReadableCell> allCells = new ArrayList<>(nonIntersectingCells);
        allCells.addAll(cellsIntersectingRow);
        return new ReadableRow(rowNumber, sortedByColumn(allCells));
    }

    private List<SimpleCell> getSimpleCells(Row actualRow) {
        List<SimpleCell> cells = new ArrayList<>();
        for (Cell cell : actualRow) {
            cells.add(new SimpleCell(cell, cellValueFormatter.toString(cell)));
        }
        return cells;
    }

    private List<SimpleCell> removeIntersectingCells(List<SimpleCell> cells, List<MergedCell> prioritizedCells) {
        List<SimpleCell> uniqueCells = new ArrayList<>();
        for (SimpleCell cell : cells) {
            boolean cellDoesNotIntersectAnyMergeRegion = prioritizedCells.stream()
                    .noneMatch(mergedCell -> mergedCell.isWithinBounds(cell.getRowNumber(), cell.getColumnNumber()));
            if (cellDoesNotIntersectAnyMergeRegion) {
                uniqueCells.add(cell);
            }
        }
        return uniqueCells;
    }

    private static List<ReadableCell> sortedByColumn(List<? extends ReadableCell> cells) {
        return cells.stream()
                .sorted(Comparator.comparingInt(ReadableCell::getColumnNumber))
                .collect(Collectors.toList());
    }

    public ReadableCell getCell(String cellReference) {
        CellReference reference = new CellReference(cellReference);
        return getCell(reference.getRow(), reference.getCol());
    }

    public ReadableCell getCell(int rowIndex, int columnIndex) {
        ReadableRow row = getRow(rowIndex);
        return row.getCell(columnIndex);
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
                return getRow(row.getRowNum());
            }
        };
    }
}
