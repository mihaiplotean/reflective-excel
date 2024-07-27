package com.reflectiveexcel.reader.table;

import java.time.LocalDateTime;
import java.util.Date;

import com.reflectiveexcel.reader.workbook.sheet.ReadableCell;
import org.apache.poi.ss.usermodel.CellType;

public class DummyCell implements ReadableCell {

    @Override
    public String getValue() {
        return "";
    }

    @Override
    public CellType getValueType() {
        return CellType.STRING;
    }

    @Override
    public int getRowNumber() {
        return 0;
    }

    @Override
    public int getColumnNumber() {
        return 0;
    }

    @Override
    public Date getDateValue() {
        return null;
    }

    @Override
    public double getDoubleValue() {
        return 0d;
    }

    @Override
    public LocalDateTime getLocalDateTimeValue() {
        return null;
    }

    @Override
    public String getCellReference() {
        return null;
    }

    @Override
    public int getStartRow() {
        return 0;
    }

    @Override
    public int getEndRow() {
        return 0;
    }

    @Override
    public int getStartColumn() {
        return 0;
    }

    @Override
    public int getEndColumn() {
        return 0;
    }
}
