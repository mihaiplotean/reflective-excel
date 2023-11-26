package com.mihai;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellReference;

public class CellDetails {

    private final String columnName;
    private final String cellValue;
    private final CellType cellType;
    private final int rowIndex;
    private final int columnIndex;
    private final String cellReference;

    public CellDetails(CellDetailsBuilder builder) {
        this.columnName = builder.columnName;
        this.cellValue = builder.cellValue;
        this.cellType = builder.cellType;
        this.rowIndex = builder.rowIndex;
        this.columnIndex = builder.columnIndex;
        this.cellReference = new CellReference(builder.rowIndex, builder.columnIndex).formatAsString();
    }

    public String getColumnName() {
        return columnName;
    }

    public String getCellValue() {
        return cellValue;
    }

    public Object getCellType() {
        return cellType;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public String getCellReference() {
        return cellReference;
    }

    public static class CellDetailsBuilder {
        private String columnName;
        private String cellValue;
        private CellType cellType;
        private int rowIndex;
        private int columnIndex;

        public CellDetailsBuilder columnName(String columnName) {
            this.columnName = columnName;
            return this;
        }

        public CellDetailsBuilder cellValue(String cellValue) {
            this.cellValue = cellValue;
            return this;
        }

        public CellDetailsBuilder cellType(CellType cellType) {
            this.cellType = cellType;
            return this;
        }

        public CellDetailsBuilder rowIndex(int rowIndex) {
            this.rowIndex = rowIndex;
            return this;
        }

        public CellDetailsBuilder columnIndex(int columnIndex) {
            this.columnIndex = columnIndex;
            return this;
        }

        public CellDetails build() {
            return new CellDetails(this);
        }
    }
}
