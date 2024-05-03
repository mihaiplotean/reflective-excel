package com.mihai.reader.workbook.sheet;

import com.mihai.writer.locator.CellLocation;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class ReadableCell {

    private final Cell cell;
    private final BoundedCell boundedCell;
    private final String cellValue;
    private final String cellReference;

    public ReadableCell(Cell cell, BoundedCell boundedCell, String cellValue) {
        this.cell = cell;
        this.boundedCell = boundedCell;
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
        return boundedCell.valueCell().getDateCellValue();
    }

    public LocalDateTime getLocalDateTimeValue() {
        return boundedCell.valueCell().getLocalDateTimeCellValue();
    }

    public String getCellReference() {
        return cellReference;
    }

    public boolean equalLocation(ReadableCell other) {
        return getRowNumber() == other.getRowNumber() && getColumnNumber() == other.getColumnNumber();
    }

    public boolean equalBounds(ReadableCell other) {
        return Objects.equals(boundedCell, other.boundedCell);
    }

    public int getBoundStartRow() {
        return boundedCell.startRow();
    }

    public int getBoundEndRow() {
        return boundedCell.endRow();
    }

    public int getBoundStartColumn() {
        return boundedCell.startColumn();
    }

    public int getBoundEndColumn() {
        return boundedCell.endColumn();
    }

    public CellLocation getLocation() {
        return new CellLocation(getRowNumber(), getColumnNumber());
    }
}
