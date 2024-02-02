package com.mihai;

import com.mihai.workbook.sheet.PropertyCell;
import com.mihai.workbook.sheet.ReadableSheet;
import com.mihai.workbook.sheet.RowCells;

import java.util.Iterator;
import java.util.List;

public class TableRowCellPointer {

    private final ReadableSheet sheet;

    private TableHeaders currentTableHeaders;

    private RowCells currentRowCells;
    private PropertyCell currentCell;

    private Iterator<RowCells> rowIterator;
    private Iterator<PropertyCell> cellIterator;

    public TableRowCellPointer(ReadableSheet sheet) {
        this.sheet = sheet;
        this.rowIterator = sheet.iterator();
    }

    public ReadableSheet getSheet() {
        return sheet;
    }

    public int getCurrentRowNumber() {
        if(currentRowCells == null) {
            return -1;
        }
        return currentRowCells.getRowNumber();
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

    public RowCells getCurrentRow() {
        return currentRowCells;
    }

    public PropertyCell getCurrentCell() {
        return currentCell;
    }

    public RowCells nextRow() {
        currentRowCells = boundToCurrentTable(rowIterator.next());
        cellIterator = currentRowCells.iterator();
        return currentRowCells;
    }

    public RowCells boundToCurrentTable(RowCells rowCells) {
        if(currentTableHeaders == null) {
            return rowCells;
        }
        List<PropertyCell> tableRowCells = rowCells.stream()
                .filter(cell -> currentTableHeaders.getHeader(cell.getColumnNumber()) != null)
                .toList();
        return new RowCells(rowCells.getRowNumber(), tableRowCells);
    }

    public boolean moreRowsExist() {
        return rowIterator.hasNext();
    }

    public PropertyCell nextCell() {
        currentCell = cellIterator.next();
        return currentCell;
    }

    public boolean moreCellsInRowExist() {
        return cellIterator.hasNext();
    }

    public void reset() {
        this.rowIterator = sheet.iterator();
        this.currentTableHeaders = null;
        this.currentRowCells = null;
        this.currentCell = null;
        this.cellIterator = null;
    }
}
