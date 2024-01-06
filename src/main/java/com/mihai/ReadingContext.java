package com.mihai;

import com.mihai.deserializer.DeserializationContext;
import com.mihai.workbook.PropertyCell;
import com.mihai.workbook.ReadableSheet;
import com.mihai.workbook.RowCells;

public class ReadingContext {

    private final ReadableSheet sheet;
    private final TableRowCellPointer cellPointer;
    private final DeserializationContext deserializationContext;

    public ReadingContext(TableRowCellPointer cellPointer, DeserializationContext deserializationContext) {
        this.sheet = cellPointer.getSheet();
        this.cellPointer = cellPointer;
        this.deserializationContext = deserializationContext;
    }

    public TableHeaders getCurrentTableHeaders() {
        return cellPointer.getCurrentTableHeaders();
    }

    public RowCells getCurrentRow() {
        return cellPointer.getCurrentRow();
    }

    public PropertyCell getCurrentCell() {
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
        return deserializationContext.deserialize(clazz, getCurrentCell());
    }

    public String getCellValue(String cellReference) {
        PropertyCell cell = sheet.getCell(cellReference);
        if (cell == null) {
            return null;
        }
        return cell.getValue();
    }

    public String getCellValue(int row, int column) {
        PropertyCell cell = sheet.getCell(row, column);
        if (cell == null) {
            return null;
        }
        return cell.getValue();
    }

    public <T> T getCellValue(String cellReference, Class<T> clazz) {
        PropertyCell cell = sheet.getCell(cellReference);
        if (cell == null) {
            return null;
        }
        return deserializationContext.deserialize(clazz, cell);
    }

    public <T> T getCellValue(int row, int column, Class<T> clazz) {
        PropertyCell cell = sheet.getCell(row, column);
        if (cell == null) {
            return null;
        }
        return deserializationContext.deserialize(clazz, cell);
    }
}
