package com.mihai.reader;

import com.mihai.reader.bean.RootTableBeanNode;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import com.mihai.reader.deserializer.DeserializationContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.exception.BadInputExceptionConsumer;
import com.mihai.reader.workbook.sheet.ReadableRow;

import java.util.List;

public class ReadingContext {

    private final TableReadingContext tableReadingContext;
    private final CellReadingContext cellReadingContext;

    private final ReadableSheet sheet;
    private final DeserializationContext deserializationContext;
    private final BadInputExceptionConsumer exceptionConsumer;
    private final DeserializedCellValues cellValues;

    public ReadingContext(ReadableSheet sheet,
                          TableReadingContext tableReadingContext,
                          CellReadingContext cellReadingContext,
                          DeserializationContext deserializationContext,
                          BadInputExceptionConsumer exceptionConsumer) {
        this.sheet = sheet;
        this.tableReadingContext = tableReadingContext;
        this.cellReadingContext = cellReadingContext;
        this.deserializationContext = deserializationContext;
        this.exceptionConsumer = exceptionConsumer;
        this.cellValues = new DeserializedCellValues();
    }

    public String getCurrentTableId() {
        return tableReadingContext.getCurrentTableId();
    }

    public RootTableBeanNode getCurrentTableBean() {
        return tableReadingContext.getCurrentBeanNode();  // todo: implement
    }

    public TableHeaders getCurrentTableHeaders() {
        return tableReadingContext.getCurrentTableHeaders();
    }

    public ReadableRow getCurrentRow() {
        return sheet.getRow(cellReadingContext.getCurrentRow());
    }

    public ReadableRow getRow(int rowNumber) {
        return sheet.getRow(rowNumber);
    }

    public ReadableRow getCurrentTableRow() {
        return tableReadingContext.boundToCurrentTable(getCurrentRow());
    }

    public ReadableRow getCurrentTableRow(int rowNumber) {
        return tableReadingContext.boundToCurrentTable(sheet.getRow(rowNumber));
    }

    public ReadableCell getCurrentCell() {
        return sheet.getCell(cellReadingContext.getCurrentRow(), cellReadingContext.getCurrentColumn());
    }

    public int getCurrentRowNumber() {
        return cellReadingContext.getCurrentRow();
    }

    public int getCurrentColumnNumber() {
        return cellReadingContext.getCurrentColumn();
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

    private <T> T deserialize(ReadableCell cell, Class<T> clazz) {  // todo: move into ReadableSheet?
        int row = cell.getRowNumber();
        int column = cell.getColumnNumber();

        T cachedValue = cellValues.getValue(row, column, clazz);
        if (cachedValue != null) {
            return cachedValue;
        }

        try {
            T value = deserializationContext.deserialize(this, clazz, cell);
            cellValues.putValue(row, column, clazz, value);
            return value;
        } catch (BadInputException exception) {
            exceptionConsumer.accept(this, exception);
        }
        return null;
    }
}
