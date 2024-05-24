package com.mihai.reader;

import com.mihai.common.CellPointer;
import com.mihai.reader.bean.RootTableBeanReadNode;
import com.mihai.reader.table.DeserializedCellValues;
import com.mihai.reader.table.ReadTable;
import com.mihai.reader.table.TableHeaders;
import com.mihai.reader.table.TableReadingContext;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import com.mihai.reader.deserializer.DeserializationContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.exception.BadInputExceptionConsumer;
import com.mihai.reader.workbook.sheet.ReadableRow;

public class ReadingContext {

    private final TableReadingContext tableReadingContext;
    private final CellPointer cellPointer;

    private final ReadableSheet sheet;
    private final DeserializationContext deserializationContext;
    private final BadInputExceptionConsumer exceptionConsumer;
    private final DeserializedCellValues cellValues;

    public ReadingContext(ReadableSheet sheet,
                          TableReadingContext tableReadingContext,
                          CellPointer cellPointer,
                          DeserializationContext deserializationContext,
                          BadInputExceptionConsumer exceptionConsumer) {
        this.sheet = sheet;
        this.tableReadingContext = tableReadingContext;
        this.cellPointer = cellPointer;
        this.deserializationContext = deserializationContext;
        this.exceptionConsumer = exceptionConsumer;
        this.cellValues = new DeserializedCellValues();
    }

    public String getCurrentTableId() {
        return tableReadingContext.getCurrentTableId();
    }

    public RootTableBeanReadNode getCurrentTableBean() {
        return tableReadingContext.getCurrentBeanNode();
    }

    public ReadTable getLastReadTable() {
        return tableReadingContext.getLastReadTable();
    }

    public ReadTable getTable(String tableId) {
        return tableReadingContext.getTable(tableId);
    }

    public TableHeaders getCurrentTableHeaders() {
        return tableReadingContext.getCurrentTableHeaders();
    }

    public ReadableRow getCurrentRow() {
        return sheet.getRow(cellPointer.getCurrentRow());
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
        return sheet.getCell(cellPointer.getCurrentRow(), cellPointer.getCurrentColumn());
    }

    public int getCurrentRowNumber() {
        return cellPointer.getCurrentRow();
    }

    public int getCurrentColumnNumber() {
        return cellPointer.getCurrentColumn();
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
        if (cachedValue != null) {
            return cachedValue;
        }

        int currentRow = cellPointer.getCurrentRow();
        int currentColumn = cellPointer.getCurrentColumn();

        cellPointer.setCurrentRow(row);
        cellPointer.setCurrentColumn(column);
        try {
            T value = deserializationContext.deserialize(this, clazz, cell);
            cellValues.putValue(row, column, clazz, value);
            return value;
        } catch (BadInputException exception) {
            exceptionConsumer.accept(this, exception);
        }
        finally {
            cellPointer.setCurrentRow(currentRow);
            cellPointer.setCurrentColumn(currentColumn);
        }
        return null;
    }
}
