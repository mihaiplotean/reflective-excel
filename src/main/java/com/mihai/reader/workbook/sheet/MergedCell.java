package com.mihai.reader.workbook.sheet;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import com.mihai.core.workbook.Bounds;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

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
    public CellType getValueType() {
        return cell.getCellType();
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
    public double getDoubleValue() {
        return cell.getNumericCellValue();
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
        return bounds.startRow();
    }

    @Override
    public int getEndRow() {
        return bounds.endRow();
    }

    @Override
    public int getStartColumn() {
        return bounds.startColumn();
    }

    @Override
    public int getEndColumn() {
        return bounds.endColumn();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MergedCell that = (MergedCell) o;
        return Objects.equals(cell, that.cell)
                && Objects.equals(bounds, that.bounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell, bounds);
    }
}
