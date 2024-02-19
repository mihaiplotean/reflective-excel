package com.mihai.reader;

import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import com.mihai.reader.workbook.sheet.CellBounds;
import com.mihai.reader.workbook.sheet.ReadableRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableHeaderReader {

    private final ReadableSheet sheet;
    private final Map<CellBounds, TableHeader> headerPerBounds = new HashMap<>();

    public TableHeaderReader(ReadableSheet sheet) {
        this.sheet = sheet;
    }

    public TableHeaders read(ReadableRow lastHeaderRow, List<ReadableCell> headerCells) {
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
        CellBounds cellBounds = getCellBounds(headerCell);
        TableHeader tableHeader = headerPerBounds.get(cellBounds);
        if (boundStartRow <= startRow) {
            if(tableHeader == null) {
                tableHeader = new TableHeader(headerCell);
                headerPerBounds.put(cellBounds, tableHeader);
            }
            return tableHeader;
        }

        if(tableHeader == null) {
            TableHeader parent = buildHeader(startRow, getCellAbove(headerCell));
            tableHeader = new TableHeader(headerCell);
            tableHeader.setParent(parent);
            parent.addChildHeader(tableHeader);
            headerPerBounds.put(cellBounds, tableHeader);
        }
        return tableHeader;
    }

    private CellBounds getCellBounds(ReadableCell cell) {
        return sheet.getCellBounds(cell.getRowNumber(), cell.getColumnNumber());
    }

    private ReadableCell getCellAbove(ReadableCell cell) {
        int boundStartRow = cell.getBoundStartRow();
        return sheet.getCell(boundStartRow - 1, cell.getColumnNumber());
    }
}
