package com.mihai.workbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.time.LocalDateTime;
import java.util.Date;

public class PropertyCell {

    private final Cell cell;
    private final String cellValue;
    private final String cellReference;

    public PropertyCell(Cell cell, String cellValue) {
        this.cell = cell;
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
}
