package com.mihai.writer;

import com.mihai.writer.node.AnnotatedFieldNode;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.WritableCellStyle;

public class HeaderWriter {

    private final CellWriter cellWriter;
    private final SerializationContext serializationContext;
    private final CellStyleContext styleContext;

    public HeaderWriter(CellWriter cellWriter, SerializationContext serializationContext, CellStyleContext styleContext) {
        this.cellWriter = cellWriter;
        this.serializationContext = serializationContext;
        this.styleContext = styleContext;
    }

    public RootFieldNode writeHeaders(Class<?> clazz, Object firstRow) {
        RootFieldNode rootNode = new RootFieldNode(clazz, firstRow);
        int currentColumn = 0;
        for (AnnotatedFieldNode child : rootNode.getChildren()) {
            writeHeader(child, rootNode.getHeight() + 1, 0, currentColumn);
            currentColumn += child.getLength();
        }
        return rootNode;
    }

    private void writeHeader(AnnotatedFieldNode node, int headerHeight, int currentRow, int currentColumn) {
        if (node.getHeight() > 0 && node.getLength() > 0) {
            Object valueToWrite = serializationContext.serialize((Class<Object>) node.getType(), node.getName());
            WritableCellStyle style = styleContext.getHeaderStyle(null, node.getName());

            WritableCell cell = new WritableCell(valueToWrite,
                    currentRow,
                    currentColumn,
                    currentRow + headerHeight - node.getHeight() - 1,
                    currentColumn + node.getLength() - 1);
            cellWriter.writeCell(cell, style);

            currentRow += headerHeight - node.getHeight();
        }
        for (AnnotatedFieldNode child : node.getChildren()) {
            writeHeader(child, headerHeight - 1, currentRow, currentColumn);
            currentColumn += child.getLength();
        }
    }
}
