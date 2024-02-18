package com.mihai;

import com.mihai.workbook.sheet.ReadableCell;
import com.mihai.workbook.sheet.ReadableSheet;
import com.mihai.workbook.sheet.ReadableRow;

import java.util.Iterator;
import java.util.List;

public class TableRowCellPointer {

    private final ReadableSheet sheet;

    private TableHeaders currentTableHeaders;

    private ReadableRow currentRow;
    private ReadableCell currentCell;

    private Iterator<ReadableRow> rowIterator;
    private Iterator<ReadableCell> cellIterator;

    public TableRowCellPointer(ReadableSheet sheet) {
        this.sheet = sheet;
        this.rowIterator = sheet.iterator();
    }

    public ReadableSheet getSheet() {
        return sheet;
    }

    public int getCurrentRowNumber() {
        if(currentRow == null) {
            return -1;
        }
        return currentRow.getRowNumber();
    }

    public int getCurrentColumnNumber() {
        if(currentCell == null) {
            return -1;
        }
        return currentCell.getColumnNumber();
    }

    public TableHeaders getCurrentTableHeaders() {
        return currentTableHeaders;
    }

    public void setCurrentTableHeaders(TableHeaders currentTableHeaders) {
        this.currentTableHeaders = currentTableHeaders;
    }

    public ReadableRow getCurrentRow() {
        return currentRow;
    }

    public ReadableCell getCurrentCell() {
        return currentCell;
    }

    public ReadableRow nextRow() {
        currentRow = boundToCurrentTable(rowIterator.next());
        cellIterator = currentRow.iterator();
        return currentRow;
    }

    public ReadableRow boundToCurrentTable(ReadableRow row) {
        if(currentTableHeaders == null) {
            return row;
        }
        List<ReadableCell> tableRowCells = row.stream()
                .filter(cell -> currentTableHeaders.getHeader(cell.getColumnNumber()) != null)
                .toList();
        return new ReadableRow(row.getRowNumber(), tableRowCells);
    }

    public boolean moreRowsExist() {
        return rowIterator.hasNext();
    }

    public ReadableCell nextCell() {
        currentCell = cellIterator.next();
        return currentCell;
    }

    public boolean moreCellsInRowExist() {
        return cellIterator.hasNext();
    }

    public void reset() {
        this.rowIterator = sheet.iterator();
        this.currentTableHeaders = null;
        this.currentRow = null;
        this.currentCell = null;
        this.cellIterator = null;
    }
}
