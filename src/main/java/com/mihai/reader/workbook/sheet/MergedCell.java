package com.mihai.reader.workbook.sheet;

import com.mihai.common.workbook.Bounds;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public final class MergedCell implements ReadableCell {

    private final Cell cell;
    private final String cellValue;
    private final Bounds bounds;
    private final String cellReference;

    public MergedCell(Cell cell, String cellValue, Bounds bounds) {
        this.cell = cell;
        this.cellValue = cellValue;
        this.bounds = bounds;
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
        return bounds.getStartRow();
    }

    @Override
    public int getEndRow() {
        return bounds.getEndRow();
    }

    @Override
    public int getStartColumn() {
        return bounds.getStartColumn();
    }

    @Override
    public int getEndColumn() {
        return bounds.getEndColumn();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MergedCell that = (MergedCell) o;
        return Objects.equals(cell, that.cell)
                && Objects.equals(bounds, that.bounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell, bounds);
    }
}
