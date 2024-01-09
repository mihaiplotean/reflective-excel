package com.mihai;

import com.mihai.workbook.sheet.CellBounds;
import com.mihai.workbook.sheet.PropertyCell;
import com.mihai.workbook.sheet.ReadableSheet;
import com.mihai.workbook.sheet.RowCells;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableHeaderReader {

    private final ReadableSheet sheet;
    private final Map<CellBounds, TableHeader> headerPerBounds = new HashMap<>();

    public TableHeaderReader(ReadableSheet sheet) {
        this.sheet = sheet;
    }

    public TableHeaders read(RowCells lastHeaderRow, List<PropertyCell> headerCells) {
        int startRow = headerCells.stream()
                .map(PropertyCell::getBoundStartRow)
                .min(Integer::compare)
                .orElse(0);

        List<TableHeader> headers = lastHeaderRow.stream()
                .filter(cell -> withinColumnBound(cell, headerCells))
                .map(cell -> buildHeader(startRow, cell))
                .toList();

        return new TableHeaders(headers);
    }

    private boolean withinColumnBound(PropertyCell cell, List<PropertyCell> cells) {
        return cells.stream()
                .anyMatch(headerCell -> headerCell.getBoundStartColumn() <= cell.getBoundStartColumn()
                        && cell.getBoundEndColumn() <= headerCell.getBoundEndColumn());
    }

    private TableHeader buildHeader(int startRow, PropertyCell headerCell) {
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

    private CellBounds getCellBounds(PropertyCell cell) {
        return sheet.getCellBounds(cell.getRowNumber(), cell.getColumnNumber());
    }

    private PropertyCell getCellAbove(PropertyCell cell) {
        int boundStartRow = cell.getBoundStartRow();
        return sheet.getCell(boundStartRow - 1, cell.getColumnNumber());
    }
}
