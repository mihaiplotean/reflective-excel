package com.mihai;

import com.mihai.workbook.sheet.PropertyCell;
import com.mihai.workbook.sheet.ReadableSheet;
import com.mihai.workbook.sheet.RowCells;

import java.util.Iterator;

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
        currentRowCells = rowIterator.next();
        cellIterator = currentRowCells.iterator();
        return currentRowCells;
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
}
