package com.mihai.reader.workbook.sheet;

import com.mihai.writer.locator.CellLocation;

import java.time.LocalDateTime;
import java.util.Date;

public interface ReadableCell {

    String getValue();

    int getRowNumber();

    int getColumnNumber();

    Date getDateValue();

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
