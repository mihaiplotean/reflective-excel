package com.mihai.reader.readers;

import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.table.TableHeader;
import com.mihai.reader.table.TableHeaders;
import com.mihai.reader.detector.TableRowColumnDetector;
import com.mihai.reader.workbook.sheet.BoundedCell;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;

import java.util.*;

public class TableHeaderReader {

    private final ReadableSheetContext sheetContext;
    private final TableRowColumnDetector rowColumnDetector;
    private final Map<BoundedCell, TableHeader> headerPerBounds = new HashMap<>();

    public TableHeaderReader(ReadableSheetContext sheetContext, TableRowColumnDetector rowColumnDetector) {
        this.sheetContext = sheetContext;
        this.rowColumnDetector = rowColumnDetector;
    }

    public TableHeaders readHeaders() {
        ReadableRow row = goToHeaderRow();
        if(row == null) {
            return new TableHeaders(List.of());
        }

        int startColumn = getStartColumn(row);
        int endColumn = getEndColumn(row, startColumn);

        List<ReadableCell> headerCells = new ArrayList<>();
        for (ReadableCell cell : row) {
            int columnNumber = cell.getColumnNumber();
            if(startColumn <= columnNumber && columnNumber <= endColumn) {
                headerCells.add(cell);
            }
        }

        int endRow = headerCells.stream()
                .map(ReadableCell::getBoundEndRow)
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
            if(rowColumnDetector.isHeaderStartColumn(sheetContext.getReadingContext(), cell)) {
                return cell.getColumnNumber();
            }
        }
        return Integer.MAX_VALUE;
    }

    private int getEndColumn(ReadableRow row, int startColumn) {
        Iterator<ReadableCell> cellIterator = sheetContext.createCellIterator(row);
        while (cellIterator.hasNext()) {
            ReadableCell cell = cellIterator.next();
            if(cell.getColumnNumber() < startColumn) {
                continue;
            }
            if(rowColumnDetector.isHeaderLastColumn(sheetContext.getReadingContext(), cell)) {
                return cell.getColumnNumber();
            }
        }
        return Integer.MIN_VALUE;
    }

    private TableHeaders read(List<ReadableCell> headerCells) {
        ReadableRow lastHeaderRow = sheetContext.getCurrentRow();
        int startRow = headerCells.stream()
                .map(ReadableCell::getBoundStartRow)
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
                .anyMatch(headerCell -> headerCell.getBoundStartColumn() <= cell.getBoundStartColumn()
                        && cell.getBoundEndColumn() <= headerCell.getBoundEndColumn());
    }

    private TableHeader buildHeader(int startRow, ReadableCell headerCell) {
        int boundStartRow = headerCell.getBoundStartRow();
        BoundedCell boundedCell = getCellBounds(headerCell);
        TableHeader tableHeader = headerPerBounds.get(boundedCell);
        if (boundStartRow <= startRow) {
            if(tableHeader == null) {
                tableHeader = new TableHeader(headerCell);
                headerPerBounds.put(boundedCell, tableHeader);
            }
            return tableHeader;
        }

        if(tableHeader == null) {
            TableHeader parent = buildHeader(startRow, getCellAbove(headerCell));
            tableHeader = new TableHeader(headerCell);
            tableHeader.setParent(parent);
            parent.addChildHeader(tableHeader);
            headerPerBounds.put(boundedCell, tableHeader);
        }
        return tableHeader;
    }

    private BoundedCell getCellBounds(ReadableCell cell) {
        return sheetContext.getSheet().getCellBounds(cell.getRowNumber(), cell.getColumnNumber());
    }

    private ReadableCell getCellAbove(ReadableCell cell) {
        int boundStartRow = cell.getBoundStartRow();
        return sheetContext.getSheet().getCell(boundStartRow - 1, cell.getColumnNumber());
    }
}
