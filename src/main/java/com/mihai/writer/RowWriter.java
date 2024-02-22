package com.mihai.writer;

import com.mihai.writer.node.AnnotatedFieldNodeInterface;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.node.TypedValue;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.WritableCellStyle;

import java.util.List;

public class RowWriter {

    private final RootFieldNode rootNode;
    private final CellWriter cellWriter;
    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;

    private int currentRow;

    public RowWriter(RootFieldNode rootNode, CellWriter cellWriter, SerializationContext serializationContext,
                     CellStyleContext cellStyleContext) {
        this.rootNode = rootNode;
        this.cellWriter = cellWriter;
        this.serializationContext = serializationContext;
        this.cellStyleContext = cellStyleContext;
        this.currentRow = rootNode.getHeight();
    }

    public void writeRow(Object row) {
        WritableCellStyle rowStyle = cellStyleContext.getRowStyle(null, row);
        int currentColumn = 0;
        for (AnnotatedFieldNodeInterface tableHeader : rootNode.getChildren()) {
            List<TypedValue> leafValues = tableHeader.getLeafValues(row);
            for (TypedValue typedValue : leafValues) {
                Object valueToWrite = serializationContext.serialize((Class<Object>) typedValue.getType(), typedValue.getValue());
                WritableCellStyle columnStyle = cellStyleContext.getColumnStyle(null, (Class<Object>) typedValue.getType(), typedValue.getValue());

                WritableCell cell = new WritableCell(valueToWrite, currentRow, currentColumn);
                cellWriter.writeCell(cell, List.of(columnStyle, rowStyle));
                currentColumn++;
            }
        }
        currentRow++;
    }
}
