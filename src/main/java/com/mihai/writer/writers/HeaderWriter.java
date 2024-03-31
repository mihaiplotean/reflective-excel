package com.mihai.writer.writers;

import com.mihai.writer.SheetContext;
import com.mihai.writer.WritableCell;
import com.mihai.writer.locator.CellLocation;
import com.mihai.writer.node.AnnotatedFieldNode;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.table.WrittenTableHeader;
import com.mihai.writer.table.WrittenTableHeaders;

import java.util.ArrayList;
import java.util.List;

public class HeaderWriter {

    private final CellWriter cellWriter;
    private final SheetContext sheetContext;

    public HeaderWriter(CellWriter cellWriter, SheetContext sheetContext) {
        this.cellWriter = cellWriter;
        this.sheetContext = sheetContext;
    }

    public WrittenTableHeaders writeHeaders(RootFieldNode rootNode) {
        int currentColumn = 0;
        List<WrittenTableHeader> leafHeaders = new ArrayList<>();
        for (AnnotatedFieldNode child : rootNode.getChildren()) {
            List<WrittenTableHeader> headers = writeHeader(child, rootNode.getHeight() + 1, 0, currentColumn);
            currentColumn += child.getLength();
            leafHeaders.addAll(headers);
        }
        sheetContext.resetCellPointer();
        return new WrittenTableHeaders(rootNode.getHeight() - 1, leafHeaders);
    }

    private List<WrittenTableHeader> writeHeader(AnnotatedFieldNode node, int headerHeight, int currentRow, int currentColumn) {
        List<WrittenTableHeader> headers = new ArrayList<>();
        writeHeader(node, headerHeight, currentRow, currentColumn, headers);
        return headers;
    }

    private void writeHeader(AnnotatedFieldNode node, int headerHeight, int currentRow, int currentColumn,
                             List<WrittenTableHeader> leafHeaders) {
        if (node.getHeight() > 0 && node.getLength() > 0) {
            Object valueToWrite = sheetContext.serialize(node.getName().getClass(), node.getName());

            sheetContext.setCurrentRow(currentRow + cellWriter.getOffsetRows());
            sheetContext.setCurrentColumn(currentColumn + cellWriter.getOffsetColumns());

            WritableCellStyle style = sheetContext.getHeaderStyle(node.getName());
            WritableCellStyle cellStyle = sheetContext.getCellStyle(node.getName());
            WritableCellStyle typeStyle = sheetContext.getTypeStyle(node.getName());

            WritableCell cell = new WritableCell(valueToWrite,
                    currentRow,
                    currentColumn,
                    currentRow + headerHeight - node.getHeight() - 1,
                    currentColumn + node.getLength() - 1);
            CellLocation cellLocation = cellWriter.writeCell(cell, List.of(style, cellStyle, typeStyle));
            currentRow += headerHeight - node.getHeight();

            if(node.isLeafValue()) {
                leafHeaders.add(new WrittenTableHeader(String.valueOf(valueToWrite), cellLocation.getColumn()));
            }
        }
        for (AnnotatedFieldNode child : node.getChildren()) {
            writeHeader(child, node.getHeight() > 0 ? headerHeight - 1 : headerHeight, currentRow, currentColumn, leafHeaders);
            currentColumn += child.getLength();
        }
    }
}
