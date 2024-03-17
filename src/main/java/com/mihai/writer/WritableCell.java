package com.mihai.writer;

import com.mihai.writer.locator.CellLocation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class WritableCell {

    private final Object value;
    private final int startRow;
    private final int startColumn;
    private final int endRow;
    private final int endColumn;

    public WritableCell(Object value, int row, int column) {
        this(value, row, column, row, column);
    }

    public WritableCell(Object value, int startRow, int startColumn, int endRow, int endColumn) {
        this.value = value;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
    }

    public Object getValue() {
        return value;
    }

//    public WritableCellStyle getStyle() {
//        return style;
//    }

    public void writeTo(Cell cell) {
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    public boolean spansMultipleCells() {
        return !spansOneCell();
    }

    public boolean spansOneCell() {
        return endRow - startRow == 0 && endColumn - startColumn == 0;
    }

    public CellRangeAddress getCellRangeAddress() {
        return new CellRangeAddress(startRow, endRow, startColumn, endColumn);
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public CellLocation getLocation() {
        return new CellLocation(startRow, startColumn);
    }
}
