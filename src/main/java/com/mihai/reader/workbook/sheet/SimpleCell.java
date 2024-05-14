package com.mihai.reader.workbook.sheet;

import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class SimpleCell implements ReadableCell {

    private final Cell cell;
    private final String cellValue;
    private final String cellReference;

//    /**
//     * @param cell        the actual Apache POI Excel cell reference.
//     * @param boundedCell the object containing the bounds of the {@link #cell}, as well as the cell containing the
//     *                    value corresponding to those bounds. This is necessary, because, in Excel and POI, when cells are merged,
//     *                    only the first cell of the merged region stores the cell value. In our framework, all the cells within
//     *                    the merged region store the value, reflecting what the user actually sees in the Excel sheet.
//     * @param cellValue   the best string representation that we could achieve, i.e. what the user sees, of the cell.
//     */
    public SimpleCell(Cell cell, String cellValue) {
        this.cell = cell;
        this.cellValue = cellValue;
        this.cellReference = cell.getAddress().formatAsString();
    }

    @Override
    public String getValue() {
        return cellValue;
    }

    @Override
    public int getRowNumber() {
        return cell.getRowIndex();
    }

    @Override
    public int getColumnNumber() {
        return cell.getColumnIndex();
    }

    @Override
    public Date getDateValue() {
        return cell.getDateCellValue();
    }

    @Override
    public LocalDateTime getLocalDateTimeValue() {
        return cell.getLocalDateTimeCellValue();
    }

    @Override
    public String getCellReference() {
        return cellReference;
    }

    @Override
    public int getStartRow() {
        return getRowNumber();
    }

    @Override
    public int getEndRow() {
        return getRowNumber();
    }

    @Override
    public int getStartColumn() {
        return getColumnNumber();
    }

    @Override
    public int getEndColumn() {
        return getColumnNumber();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SimpleCell that = (SimpleCell) object;
        return Objects.equals(cell, that.cell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell);
    }
}
