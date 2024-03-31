package com.mihai.writer;

import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.table.CellWritingContext;
import com.mihai.writer.table.TableWritingContext;
import com.mihai.writer.table.WrittenTable;
import com.mihai.writer.table.WrittenTableHeaders;

public class SheetContext {

    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;

    private final WritingContext writingContext;
    private final TableWritingContext tableWritingContext;
    private final CellWritingContext cellWritingContext;

    public SheetContext(SerializationContext serializationContext, CellStyleContext cellStyleContext) {
        this.serializationContext = serializationContext;
        this.cellStyleContext = cellStyleContext;
        this.tableWritingContext = new TableWritingContext();
        this.cellWritingContext = new CellWritingContext();
        this.writingContext = new WritingContext(tableWritingContext, cellWritingContext);
    }

    public WritingContext getWritingContext() {
        return writingContext;
    }

    @SuppressWarnings("unchecked")
    public Object serialize(Class<?> clazz, Object value) {
        return serializationContext.serialize((Class<Object>) clazz, value);
    }

    public WritableCellStyle getHeaderStyle(Object headerValue) {
        return cellStyleContext.getHeaderStyle(writingContext, headerValue);
    }

    public WritableCellStyle getRowStyle(Object row) {
        return cellStyleContext.getRowStyle(writingContext, row);
    }

    public WritableCellStyle getTypeStyle(Object target) {
        return cellStyleContext.getTypeStyle(writingContext, target);
    }

    public WritableCellStyle getColumnStyle(Object target) {
        return cellStyleContext.getColumnStyle(writingContext, target);
    }

    public WritableCellStyle getCellStyle(Object target) {
        return cellStyleContext.getCellStyle(writingContext, target);
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
        cellWritingContext.setCurrentRow(row);
    }

    public void setCurrentColumn(int column) {
        cellWritingContext.setCurrentColumn(column);
    }

    public void setCurrentTableRow(int row) {
        tableWritingContext.setCurrentTableRow(row);
    }

    public void setCurrentTableColumn(int column) {
        tableWritingContext.setCurrentTableColumn(column);
    }

    public void resetCellPointer() {
        cellWritingContext.reset();
    }
}
