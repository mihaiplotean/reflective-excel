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

    /**
     * @param cell the actual Apache POI Excel cell reference.
     * @param boundedCell the object containing the bounds of the {@link #cell}, as well as the cell containing the
     *                    value corresponding to those bounds. This is necessary, because, in Excel and POI, when cells are merged,
     *                    only the first cell of the merged region stores the cell value. In our framework, all the cells within
     *                    the merged region store the value, reflecting what the user actually sees in the Excel sheet.
     * @param cellValue the best string representation that we could achieve, i.e. what the user sees, of the cell.
     */
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
