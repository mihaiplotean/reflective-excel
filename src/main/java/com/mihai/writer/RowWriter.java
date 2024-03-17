package com.mihai.writer;

import com.mihai.writer.node.AnnotatedFieldNode;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.node.TypedValue;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.table.TableWritingContext;

import java.util.List;

public class RowWriter {

    private final RootFieldNode rootNode;
    private final CellWriter cellWriter;
    private final WritingContext writingContext;
    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;

    private final TableWritingContext tableContext;
    private int currentRow;
    private int currentColumn;

    public RowWriter(RootFieldNode rootNode, CellWriter cellWriter, WritingContext writingContext, SerializationContext serializationContext,
                     CellStyleContext cellStyleContext, TableWritingContext tableContext) {
        this.rootNode = rootNode;
        this.cellWriter = cellWriter;
        this.writingContext = writingContext;
        this.serializationContext = serializationContext;
        this.cellStyleContext = cellStyleContext;
        this.currentRow = rootNode.getHeight();
        this.tableContext = tableContext;
    }
//
//    public int getCurrentRow() {
//        return currentRow;
//    }
//
//    public int getCurrentColumn() {
//        return currentColumn;
//    }
//
//    public String getColumnName() {
//        return null;
//    }

    public void writeRow(Object row) {
        WritableCellStyle rowStyle = cellStyleContext.getRowStyle(writingContext, row);
        currentColumn = 0;
        for (AnnotatedFieldNode tableHeader : rootNode.getChildren()) {
            List<TypedValue> leafValues = tableHeader.getLeafValues(row);
            for (TypedValue typedValue : leafValues) {
                updateTableContext();

                Object valueToWrite = serializationContext.serialize((Class<Object>) typedValue.getType(), typedValue.getValue());
                WritableCellStyle columnStyle = cellStyleContext.getColumnStyle(writingContext, tableHeader.getName());
                WritableCellStyle typeStyle = cellStyleContext.getTypeStyle(writingContext, typedValue.getValue());
                WritableCellStyle cellStyle = cellStyleContext.getCellStyle(writingContext, typedValue.getValue());

                WritableCell cell = new WritableCell(valueToWrite, currentRow, currentColumn);
                cellWriter.writeCell(cell, List.of(cellStyle, typeStyle, columnStyle, rowStyle));
                currentColumn++;
            }
        }
        currentRow++;
    }

    private void updateTableContext() {
        tableContext.setCurrentTableRow(currentRow);
        tableContext.setCurrentRow(currentRow + cellWriter.getOffsetRows());
        tableContext.setCurrentTableColumn(currentColumn);
        tableContext.setCurrentColumn(currentColumn + cellWriter.getOffsetColumns());
    }
}
