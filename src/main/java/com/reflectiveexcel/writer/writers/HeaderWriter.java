package com.reflectiveexcel.writer.writers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reflectiveexcel.core.workbook.CellLocation;
import com.reflectiveexcel.writer.WritableCell;
import com.reflectiveexcel.writer.WritableSheetContext;
import com.reflectiveexcel.writer.node.ChildBeanWriteNode;
import com.reflectiveexcel.writer.node.RootTableBeanWriteNode;
import com.reflectiveexcel.writer.style.WritableCellStyle;
import com.reflectiveexcel.writer.table.WrittenTableHeader;
import com.reflectiveexcel.writer.table.WrittenTableHeaders;

public class HeaderWriter {

    private final CellWriter cellWriter;
    private final WritableSheetContext sheetContext;
    private final Map<ChildBeanWriteNode, Integer> leafNodeToColumnIndexMap = new HashMap<>();

    public HeaderWriter(CellWriter cellWriter, WritableSheetContext sheetContext) {
        this.cellWriter = cellWriter;
        this.sheetContext = sheetContext;
    }

    public WrittenTableHeaders writeHeaders(RootTableBeanWriteNode rootNode) {
        int currentColumn = 0;
        List<WrittenTableHeader> leafHeaders = new ArrayList<>();
        for (ChildBeanWriteNode child : rootNode.getChildren()) {
            List<WrittenTableHeader> headers = writeHeader(child, rootNode.getHeight() + 1, currentColumn);
            currentColumn += child.getLength();
            leafHeaders.addAll(headers);
        }
        sheetContext.resetCellPointer();
        return new WrittenTableHeaders(cellWriter.getOffsetRows() + rootNode.getHeight() - 1, leafHeaders);
    }

    private List<WrittenTableHeader> writeHeader(ChildBeanWriteNode node, int headerHeight, int currentColumn) {
        List<WrittenTableHeader> headers = new ArrayList<>();
        writeHeader(node, headerHeight, 0, currentColumn, headers);
        return headers;
    }

    @SuppressWarnings("unchecked")
    private void writeHeader(ChildBeanWriteNode node, int headerHeight, int currentRow, int currentColumn,
                             List<WrittenTableHeader> leafHeaders) {
        if (node.getHeight() > 0 && node.getLength() > 0) {
            sheetContext.setCurrentRow(currentRow + cellWriter.getOffsetRows());
            sheetContext.setCurrentColumn(currentColumn + cellWriter.getOffsetColumns());

            Class<?> nodeType = node.getName().getClass();
            Object valueToWrite = sheetContext.serialize(nodeType, node.getName());

            WritableCellStyle headerStyle = sheetContext.getHeaderStyle(node.getName());
            WritableCellStyle cellStyle = sheetContext.getCellStyle(node.getName());
            WritableCellStyle typeStyle = sheetContext.getTypeStyle((Class<Object>) nodeType, node.getName());

            WritableCell cell = new WritableCell(valueToWrite,
                                                 currentRow,
                                                 currentColumn,
                                                 currentRow + headerHeight - node.getHeight() - 1,
                                                 currentColumn + node.getLength() - 1);
            CellLocation cellLocation = cellWriter.writeCell(cell, List.of(headerStyle, typeStyle, cellStyle));
            currentRow += headerHeight - node.getHeight();

            if (node.isLeafValue()) {
                leafNodeToColumnIndexMap.put(node, currentColumn + cellWriter.getOffsetColumns());
                leafHeaders.add(new WrittenTableHeader(String.valueOf(valueToWrite), cellLocation.column()));
            }
        }
        for (ChildBeanWriteNode child : node.getChildren()) {
            writeHeader(child, node.getHeight() > 0 ? headerHeight - 1 : headerHeight, currentRow, currentColumn, leafHeaders);
            currentColumn += child.getLength();
        }
    }

    public Map<ChildBeanWriteNode, Integer> getLeafNodeToColumnIndexMap() {
        return leafNodeToColumnIndexMap;
    }
}
