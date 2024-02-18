package com.mihai;

import com.mihai.deserializer.DeserializationContext;
import com.mihai.exception.BadInputException;
import com.mihai.exception.BadInputExceptionConsumer;
import com.mihai.workbook.sheet.ReadableCell;
import com.mihai.workbook.sheet.ReadableSheet;
import com.mihai.workbook.sheet.ReadableRow;

public class ReadingContext {

    private final ReadableSheet sheet;
    private final TableRowCellPointer cellPointer;
    private final DeserializationContext deserializationContext;
    private final BadInputExceptionConsumer exceptionConsumer;
    private final DeserializedCellValues cellValues;

    public ReadingContext(TableRowCellPointer cellPointer,
                          DeserializationContext deserializationContext,
                          BadInputExceptionConsumer exceptionConsumer) {
        this.sheet = cellPointer.getSheet();
        this.cellPointer = cellPointer;
        this.deserializationContext = deserializationContext;
        this.exceptionConsumer = exceptionConsumer;
        this.cellValues = new DeserializedCellValues();
    }

    public TableHeaders getCurrentTableHeaders() {
        return cellPointer.getCurrentTableHeaders();
    }

    public ReadableRow getCurrentRow() {
        return cellPointer.getCurrentRow();
    }

    public ReadableRow getRow(int rowNumber) {
        return cellPointer.boundToCurrentTable(sheet.getRow(rowNumber));
    }

    public ReadableCell getCurrentCell() {
        return cellPointer.getCurrentCell();
    }

    public int getCurrentRowNumber() {
        return cellPointer.getCurrentRowNumber();
    }

    public int getCurrentColumnNumber() {
        return cellPointer.getCurrentColumnNumber();
    }

    public String getCurrentCellValue() {
        return getCurrentCell().getValue();
    }

    public <T> T getCurrentCellValue(Class<T> clazz) {
        return deserialize(getCurrentCell(), clazz);
    }

    public String getCellValue(String cellReference) {
        ReadableCell cell = sheet.getCell(cellReference);
        if (cell == null) {
            return null;
        }
        return cell.getValue();
    }

    public String getCellValue(int row, int column) {
        ReadableCell cell = sheet.getCell(row, column);
        if (cell == null) {
            return null;
        }
        return cell.getValue();
    }

    public <T> T getCellValue(String cellReference, Class<T> clazz) {
        ReadableCell cell = sheet.getCell(cellReference);
        if (cell == null) {
            return null;
        }
        return deserialize(cell, clazz);
    }

    public <T> T getCellValue(int row, int column, Class<T> clazz) {
        ReadableCell cell = sheet.getCell(row, column);
        if (cell == null) {
            return null;
        }
        return deserialize(cell, clazz);
    }

    private <T> T deserialize(ReadableCell cell, Class<T> clazz) {
        int row = cell.getRowNumber();
        int column = cell.getColumnNumber();

        T cachedValue = cellValues.getValue(row, column, clazz);
        if(cachedValue != null) {
            return cachedValue;
        }

        try {
            T value = deserializationContext.deserialize(this, clazz, cell);
            cellValues.putValue(row, column, clazz, value);
            return value;
        }
        catch (BadInputException exception) {
            exceptionConsumer.accept(getCurrentRow(), exception);
        }
        return null;
    }
}
