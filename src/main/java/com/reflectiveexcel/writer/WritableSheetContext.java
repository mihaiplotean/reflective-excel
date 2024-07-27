package com.reflectiveexcel.writer;

import com.reflectiveexcel.core.CellPointer;
import com.reflectiveexcel.writer.serializer.SerializationContext;
import com.reflectiveexcel.writer.style.CellStyleContext;
import com.reflectiveexcel.writer.style.WritableCellStyle;
import com.reflectiveexcel.writer.table.TableWritingContext;
import com.reflectiveexcel.writer.table.WrittenTable;
import com.reflectiveexcel.writer.table.WrittenTableHeaders;

public class WritableSheetContext {

    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;

    private final WritingContext writingContext;
    private final TableWritingContext tableWritingContext;
    private final CellPointer cellPointer;

    public WritableSheetContext(SerializationContext serializationContext, CellStyleContext cellStyleContext) {
        this.serializationContext = serializationContext;
        this.cellStyleContext = cellStyleContext;
        this.tableWritingContext = new TableWritingContext();
        this.cellPointer = new CellPointer();
        this.writingContext = new WritingContext(tableWritingContext, cellPointer);
    }

    public WritingContext getWritingContext() {
        return writingContext;
    }

    @SuppressWarnings("unchecked")
    public Object serialize(Class<?> clazz, Object value) {
        return serializationContext.serialize(writingContext, (Class<Object>) clazz, value);
    }

    public WritableCellStyle getHeaderStyle(Object headerValue) {
        return cellStyleContext.getHeaderStyle(writingContext, headerValue);
    }

    public WritableCellStyle getRowStyle(Object row) {
        return cellStyleContext.getRowStyle(writingContext, row);
    }

    public <T> WritableCellStyle getTypeStyle(Class<T> clazz, T target) {
        return cellStyleContext.getTypeStyle(writingContext, clazz, target);
    }

    public WritableCellStyle getColumnStyle(Object columnName) {
        return cellStyleContext.getColumnStyle(writingContext, columnName);
    }

    public WritableCellStyle getCellStyle(Object cellValue) {
        return cellStyleContext.getCellStyle(writingContext, cellValue);
    }

    public void setWritingTable(boolean writingTable) {
        tableWritingContext.setWritingTable(writingTable);
    }

    public void setCurrentTableHeaders(WrittenTableHeaders tableHeaders) {
        tableWritingContext.setCurrentTableHeaders(tableHeaders);
    }

    public void appendTable(WrittenTable table) {
        tableWritingContext.appendTable(table);
    }

    public void setCurrentRow(int row) {
        cellPointer.setCurrentRow(row);
    }

    public void setCurrentColumn(int column) {
        cellPointer.setCurrentColumn(column);
    }

    public void setCurrentTableRow(int row) {
        tableWritingContext.setCurrentTableRow(row);
    }

    public void setCurrentTableColumn(int column) {
        tableWritingContext.setCurrentTableColumn(column);
    }

    public void resetCellPointer() {
        cellPointer.reset();
    }
}
