package com.mihai.workbook.sheet;

import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class ReadableCell {

    private final Cell cell;
    private final CellBounds cellBounds;
    private final String cellValue;
    private final String cellReference;

    public ReadableCell(Cell cell, CellBounds cellBounds, String cellValue) {
        this.cell = cell;
        this.cellBounds = cellBounds;
        this.cellValue = cellValue;
        this.cellReference = cell.getAddress().formatAsString();
    }

    public String getValue() {
        return cellValue;
    }

    public int getRowNumber() {
        return cell.getRowIndex();
    }

    public int getColumnNumber() {
        return cell.getColumnIndex();
    }

    public Date getDateValue() {
        return cellBounds.valueCell().getDateCellValue();
    }

    public LocalDateTime getLocalDateTimeValue() {
        return cellBounds.valueCell().getLocalDateTimeCellValue();
    }

    public String getCellReference() {
        return cellReference;
    }

    public boolean equalLocation(ReadableCell other) {
        return getRowNumber() == other.getRowNumber() && getColumnNumber() == other.getColumnNumber();
    }

    public boolean equalBounds(ReadableCell other) {
        return Objects.equals(cellBounds, other.cellBounds);
    }

    public int getBoundStartRow() {
        return cellBounds.startRow();
    }

    public int getBoundEndRow() {
        return cellBounds.endRow();
    }

    public int getBoundStartColumn() {
        return cellBounds.startColumn();
    }

    public int getBoundEndColumn() {
        return cellBounds.endColumn();
    }
}
