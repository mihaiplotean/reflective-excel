package com.mihai;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.time.LocalDateTime;
import java.util.Date;

public class ExcelCell {

    private final Cell cell;
    private final String cellValue;
    private final String columnName;
    private final String cellReference;

    public ExcelCell(CellDetailsBuilder builder) {
        this.cell = builder.cell;
        this.cellValue = builder.cellValue;
        this.columnName = builder.columnName;
        this.cellReference = cell.getAddress().formatAsString();
    }

    public String getValue() {
        return cellValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getRowIndex() {
        return cell.getRowIndex();
    }

    public int getColumnIndex() {
        return cell.getColumnIndex();
    }

    public CellType getCellType() {
        return cell.getCellType();
    }

    public Date getDateValue() {
        return cell.getDateCellValue();
    }

    public LocalDateTime getLocalDateTimeValue() {
        return cell.getLocalDateTimeCellValue();
    }

    public String getCellReference() {
        return cellReference;
    }

    public static class CellDetailsBuilder {
        private Cell cell;
        private String cellValue;
        private String columnName;

        public CellDetailsBuilder cell(Cell cell) {
            this.cell = cell;
            return this;
        }

        public CellDetailsBuilder cellValue(String cellValue) {
            this.cellValue = cellValue;
            return this;
        }

        public CellDetailsBuilder columnName(String columnName) {
            this.columnName = columnName;
            return this;
        }

        public ExcelCell build() {
            return new ExcelCell(this);
        }
    }
}
