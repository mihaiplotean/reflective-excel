package com.mihai.writer;

import com.mihai.writer.locator.CellLocation;
import com.mihai.writer.node.AnnotatedFieldNode;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.table.CellWritingContext;
import com.mihai.writer.table.WrittenTableHeader;
import com.mihai.writer.table.WrittenTableHeaders;

import java.util.ArrayList;
import java.util.List;

public class HeaderWriter {

    private final CellWriter cellWriter;
    private final WritingContext writingContext;
    private final SerializationContext serializationContext;
    private final CellStyleContext styleContext;

    private final CellWritingContext cellWritingContext;

    public HeaderWriter(CellWriter cellWriter, WritingContext writingContext, SerializationContext serializationContext, CellStyleContext styleContext, CellWritingContext cellWritingContext) {
        this.cellWriter = cellWriter;
        this.writingContext = writingContext;
        this.serializationContext = serializationContext;
        this.styleContext = styleContext;
        this.cellWritingContext = cellWritingContext;
    }

    public WrittenTableHeaders writeHeaders(RootFieldNode rootNode) {
        int currentColumn = 0;
        List<WrittenTableHeader> leafHeaders = new ArrayList<>();
        for (AnnotatedFieldNode child : rootNode.getChildren()) {
            List<WrittenTableHeader> headers = writeHeader(child, rootNode.getHeight() + 1, 0, currentColumn);
            currentColumn += child.getLength();
            leafHeaders.addAll(headers);
        }
        cellWritingContext.reset();
        return new WrittenTableHeaders(rootNode.getHeight() - 1, leafHeaders);
    }

    private List<WrittenTableHeader> writeHeader(AnnotatedFieldNode node, int headerHeight, int currentRow, int currentColumn) {
        List<WrittenTableHeader> headers = new ArrayList<>();
        writeHeader(node, headerHeight, currentRow, currentColumn, headers);
        return headers;
    }

    private void writeHeader(AnnotatedFieldNode node, int headerHeight, int currentRow, int currentColumn,
                             List<WrittenTableHeader> headers) {
        if (node.getHeight() > 0 && node.getLength() > 0) {
            Object valueToWrite = serializationContext.serialize((Class<Object>) node.getName().getClass(), node.getName());

            cellWritingContext.setCurrentRow(currentRow + cellWriter.getOffsetRows());
            cellWritingContext.setCurrentColumn(currentRow + cellWriter.getOffsetColumns());

            WritableCellStyle style = styleContext.getHeaderStyle(writingContext, node.getName());
            WritableCellStyle cellStyle = styleContext.getCellStyle(writingContext, node.getName());
            WritableCellStyle typeStyle = styleContext.getTypeStyle(writingContext, node.getName());

            WritableCell cell = new WritableCell(valueToWrite,
                    currentRow,
                    currentColumn,
                    currentRow + headerHeight - node.getHeight() - 1,
                    currentColumn + node.getLength() - 1);
            CellLocation cellLocation = cellWriter.writeCell(cell, List.of(style, cellStyle, typeStyle));
            currentRow += headerHeight - node.getHeight();
            headers.add(new WrittenTableHeader(String.valueOf(valueToWrite), cellLocation.getColumn()));
        }
        for (AnnotatedFieldNode child : node.getChildren()) {
            writeHeader(child, node.getHeight() > 0 ? headerHeight - 1 : headerHeight, currentRow, currentColumn);
            currentColumn += child.getLength();
        }
    }
}
