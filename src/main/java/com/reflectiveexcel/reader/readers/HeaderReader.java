package com.reflectiveexcel.reader.readers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.reflectiveexcel.reader.ReadableSheetContext;
import com.reflectiveexcel.reader.detector.TableRowColumnDetector;
import com.reflectiveexcel.reader.table.TableHeader;
import com.reflectiveexcel.reader.table.TableHeaders;
import com.reflectiveexcel.reader.workbook.sheet.ReadableCell;
import com.reflectiveexcel.reader.workbook.sheet.ReadableRow;

public class HeaderReader {

    private final ReadableSheetContext sheetContext;
    private final TableRowColumnDetector rowColumnDetector;
    private final Map<ReadableCell, TableHeader> headerPerBounds = new HashMap<>();

    public HeaderReader(ReadableSheetContext sheetContext, TableRowColumnDetector rowColumnDetector) {
        this.sheetContext = sheetContext;
        this.rowColumnDetector = rowColumnDetector;
    }

    public TableHeaders readHeaders() {
        ReadableRow row = goToHeaderRow();
        if (row == null) {
            return new TableHeaders(List.of());
        }

        int startColumn = getStartColumn(row);
        int endColumn = getEndColumn(row, startColumn);

        List<ReadableCell> headerCells = new ArrayList<>();
        for (ReadableCell cell : row) {
            int columnNumber = cell.getColumnNumber();
            if (startColumn <= columnNumber && columnNumber <= endColumn) {
                headerCells.add(cell);
            }
        }

        int endRow = headerCells.stream()
                .map(ReadableCell::getEndRow)
                .max(Integer::compare)
                .orElse(0);

        moveToRow(endRow);

        return read(headerCells);
    }

    private ReadableRow goToHeaderRow() {
        Iterator<ReadableRow> rowIterator = sheetContext.createRowIterator();
        while (rowIterator.hasNext()) {
            ReadableRow row = rowIterator.next();
            if (rowColumnDetector.isHeaderRow(sheetContext.getReadingContext(), sheetContext.getCurrentRow())) {
                return row;
            }
        }
        return null;
    }

    private void moveToRow(int endRow) {
        sheetContext.setCurrentRow(endRow);
    }

    private int getStartColumn(ReadableRow row) {
        Iterator<ReadableCell> cellIterator = sheetContext.createCellIterator(row);
        while (cellIterator.hasNext()) {
            ReadableCell cell = cellIterator.next();
            if (rowColumnDetector.isHeaderStartColumn(sheetContext.getReadingContext(), cell)) {
                return cell.getColumnNumber();
            }
        }
        return Integer.MAX_VALUE;
    }

    private int getEndColumn(ReadableRow row, int startColumn) {
        Iterator<ReadableCell> cellIterator = sheetContext.createCellIterator(row);
        while (cellIterator.hasNext()) {
            ReadableCell cell = cellIterator.next();
            if (cell.getColumnNumber() < startColumn) {
                continue;
            }
            if (rowColumnDetector.isHeaderLastColumn(sheetContext.getReadingContext(), cell)) {
                return cell.getColumnNumber();
            }
        }
        return Integer.MIN_VALUE;
    }

    private TableHeaders read(List<ReadableCell> headerCells) {
        ReadableRow lastHeaderRow = sheetContext.getCurrentRow();
        int startRow = headerCells.stream()
                .map(ReadableCell::getStartRow)
                .min(Integer::compare)
                .orElse(0);

        List<TableHeader> headers = lastHeaderRow.stream()
                .filter(cell -> withinColumnBound(cell, headerCells))
                .map(cell -> buildHeader(startRow, cell))
                .toList();

        return new TableHeaders(headers);
    }

    private boolean withinColumnBound(ReadableCell cell, List<ReadableCell> cells) {
        return cells.stream()
                .anyMatch(headerCell -> headerCell.isWithinColumnBounds(cell.getColumnNumber()));
    }

    private TableHeader buildHeader(int startRow, ReadableCell headerCell) {
        int boundStartRow = headerCell.getStartRow();
        TableHeader tableHeader = headerPerBounds.get(headerCell);
        if (boundStartRow <= startRow) {
            if (tableHeader == null) {
                tableHeader = new TableHeader(headerCell);
                headerPerBounds.put(headerCell, tableHeader);
            }
            return tableHeader;
        }

        if (tableHeader == null) {
            TableHeader parent = buildHeader(startRow, getCellAbove(headerCell));
            tableHeader = new TableHeader(headerCell);
            tableHeader.setParent(parent);
            parent.addChildHeader(tableHeader);
            headerPerBounds.put(headerCell, tableHeader);
        }
        return tableHeader;
    }

    private ReadableCell getCellAbove(ReadableCell cell) {
        int boundStartRow = cell.getStartRow();
        return sheetContext.getSheet().getCell(boundStartRow - 1, cell.getColumnNumber());
    }
}
