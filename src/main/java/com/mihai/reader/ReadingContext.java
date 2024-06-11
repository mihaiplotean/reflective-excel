package com.mihai.reader;

import com.mihai.core.CellPointer;
import com.mihai.reader.bean.RootTableBeanReadNode;
import com.mihai.reader.deserializer.DeserializationContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.exception.BadInputExceptionConsumer;
import com.mihai.reader.table.DeserializedCellValues;
import com.mihai.reader.table.ReadTable;
import com.mihai.reader.table.TableHeaders;
import com.mihai.reader.table.TableReadingContext;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;
import com.mihai.reader.workbook.sheet.ReadableSheet;

/**
 * Provides information about the state of the reader.
 */
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
                          ExcelReadingSettings settings) {
        this.sheet = sheet;
        this.tableReadingContext = tableReadingContext;
        this.cellPointer = cellPointer;
        this.deserializationContext = settings.getDeserializationContext();
        this.exceptionConsumer = settings.getExceptionConsumer();
        this.cellValues = new DeserializedCellValues();
    }

    /**
     * @return id of the table currently being read.
     * @see com.mihai.core.annotation.TableId
     */
    public String getCurrentTableId() {
        return tableReadingContext.getCurrentTableId();
    }

    /**
     * @return bean tree representation corresponding to the table being read.
     */
    public RootTableBeanReadNode getCurrentTableBean() {
        return tableReadingContext.getCurrentBeanNode();
    }

    /**
     * @return data about the last read table.
     */
    public ReadTable getLastReadTable() {
        return tableReadingContext.getLastReadTable();
    }

    /**
     * Provides information about a previously read table using its id.
     *
     * @param tableId id of the table.
     * @return data about the table.
     * @see com.mihai.core.annotation.TableId
     */
    public ReadTable getTable(String tableId) {
        return tableReadingContext.getTable(tableId);
    }

    /**
     * @return the headers of the table being read.
     */
    public TableHeaders getCurrentTableHeaders() {
        return tableReadingContext.getCurrentTableHeaders();
    }

    /**
     * Returns the full row corresponding the current row index.
     * If you need the row to only contain the cells within the table bounds, use {@link #getCurrentTableRow()}.
     *
     * @return the row that is currently being read.
     * @see #getCurrentTableRow()
     */
    public ReadableRow getCurrentRow() {
        return sheet.getRow(cellPointer.getCurrentRow());
    }

    /**
     * Returns the full row corresponding the given row index.
     * If you need the row to only contain the cells within the table bounds, use {@link #getCurrentTableRow(int)}.
     *
     * @return the row that is currently being read.
     * @see #getCurrentTableRow(int)
     */
    public ReadableRow getRow(int rowNumber) {
        return sheet.getRow(rowNumber);
    }

    /**
     * Returns the current row, bounded to the table being read.
     *
     * @return the table row that is currently being read.
     */
    public ReadableRow getCurrentTableRow() {
        return tableReadingContext.boundToCurrentTable(getCurrentRow());
    }

    /**
     * Returns the given row, bounded to the table being read. Note that this method makes sure that
     * the cells of the returned row are not outside the table headers, but it does not check if
     * the given row is after the last table row.
     *
     * @param rowNumber the index of the row.
     * @return the table row corresponding to the given index.
     */
    public ReadableRow getCurrentTableRow(int rowNumber) {
        return tableReadingContext.boundToCurrentTable(sheet.getRow(rowNumber));
    }

    /**
     * @return the cell that is currently being read.
     */
    public ReadableCell getCurrentCell() {
        return sheet.getCell(cellPointer.getCurrentRow(), cellPointer.getCurrentColumn());
    }

    /**
     * @return the row number of the row being read.
     */
    public int getCurrentRowNumber() {
        return cellPointer.getCurrentRow();
    }

    /**
     * @return the column number of the cell being read.
     */
    public int getCurrentColumnNumber() {
        return cellPointer.getCurrentColumn();
    }

    /**
     * @return the string representation of the cell currently being read.
     */
    public String getCurrentCellValue() {
        return getCurrentCell().getValue();
    }

    /**
     * Returns the value representation of the cell currently being read, deserialized to the given type.
     *
     * @param clazz the type the cell should be deserialized to.
     * @return the value of the cell currently being read.
     */
    public <T> T getCurrentCellValue(Class<T> clazz) {
        return deserialize(getCurrentCell(), clazz);
    }

    /**
     * Returns the string representation of the cell (value) at the given cell reference.
     *
     * @param cellReference reference of the cell.
     * @return string representation of the cell (value).
     */
    public String getCellValue(String cellReference) {
        ReadableCell cell = sheet.getCell(cellReference);
        if (cell == null) {
            return null;
        }
        return cell.getValue();
    }

    /**
     * Returns the string representation of the cell (value) at the given row and column.
     *
     * @param row the row of the cell.
     * @param column the column of the cell.
     * @return string representation of the cell (value).
     */
    public String getCellValue(int row, int column) {
        ReadableCell cell = sheet.getCell(row, column);
        if (cell == null) {
            return null;
        }
        return cell.getValue();
    }

    /**
     * Returns the cell (value) at the given cell reference, deserialized to the given type.
     *
     * @param cellReference reference of the cell.
     * @param clazz the type the cell should be deserialized to.
     * @return the value of the cell.
     */
    public <T> T getCellValue(String cellReference, Class<T> clazz) {
        ReadableCell cell = sheet.getCell(cellReference);
        if (cell == null) {
            return null;
        }
        return deserialize(cell, clazz);
    }

    /**
     * Returns the cell (value) at the given row and column, deserialized to the given type.
     *
     * @param row the row of the cell.
     * @param column the column of the cell.
     * @param clazz the type the cell should be deserialized to.
     * @return the value of the cell.
     */
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
        } finally {
            cellPointer.setCurrentRow(currentRow);
            cellPointer.setCurrentColumn(currentColumn);
        }
        return null;
    }
}
