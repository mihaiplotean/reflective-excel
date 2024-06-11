package com.mihai.reader.workbook.sheet;

import java.time.LocalDateTime;
import java.util.Date;

import com.mihai.core.workbook.CellLocation;
import com.mihai.reader.exception.BadInputException;

/**
* Denotes a cell, or in case of merged cells - a group of cells in the sheet.
 */
public interface ReadableCell {

    /**
     * Returns the string representation of the cell value. Note that this value might not be one-to-one with what
     * you see when opening the Excel file, as that is dependent on things such as locale and cell-style data formatting.
     *
     * @return string representation of the cell value.
     */
    String getValue();

    int getRowNumber();

    int getColumnNumber();

    /**
     * Returns the date defined in the cell. This only works if the cell value is formatted as a date number format in Excel.
     *
     * @return the date defined in the cell.
     * @throws BadInputException if the cell does not have a date numeric format.
     */
    Date getDateValue();

    /**
     * Returns the date defined in the cell. This only works if the cell value is formatted as a date number format in Excel.
     *
     * @return the date defined in the cell.
     */
    LocalDateTime getLocalDateTimeValue();

    String getCellReference();

    int getStartRow();

    int getEndRow();

    int getStartColumn();

    int getEndColumn();

    default boolean isWithinRowBounds(int row) {
        return getStartRow() <= row && row <= getEndRow();
    }

    default boolean isWithinColumnBounds(int column) {
        return getStartColumn() <= column && column <= getEndColumn();
    }

    default boolean isWithinBounds(int row, int column) {
        return isWithinRowBounds(row) && isWithinColumnBounds(column);
    }

    default CellLocation getLocation() {
        return new CellLocation(getRowNumber(), getColumnNumber());
    }
}
