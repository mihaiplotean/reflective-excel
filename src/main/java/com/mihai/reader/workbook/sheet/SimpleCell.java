package com.mihai.reader.workbook.sheet;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import com.mihai.reader.exception.BadInputException;
import org.apache.poi.ss.usermodel.Cell;

public class SimpleCell implements ReadableCell {

    private final Cell cell;
    private final String cellValue;
    private final String cellReference;

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
        try {
            return cell.getDateCellValue();
        } catch (IllegalStateException e) {
            throw new BadInputException(String.format(
                    "Value \"%s\" defined in cell %s does not have a date number format", cellValue, cellReference
            ));
        }
    }

    @Override
    public LocalDateTime getLocalDateTimeValue() {
        try {
            return cell.getLocalDateTimeCellValue();
        } catch (IllegalStateException e) {
            throw new BadInputException(String.format(
                    "Value \"%s\" defined in cell %s does not have a date number format", cellValue, cellReference
            ));
        }
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
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SimpleCell that = (SimpleCell) object;
        return Objects.equals(cell, that.cell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell);
    }
}
