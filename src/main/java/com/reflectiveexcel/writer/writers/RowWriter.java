package com.reflectiveexcel.writer.writers;

import java.util.List;

import com.reflectiveexcel.writer.WritableCell;
import com.reflectiveexcel.writer.WritableSheetContext;
import com.reflectiveexcel.writer.node.ChildBeanWriteNode;
import com.reflectiveexcel.writer.node.RootTableBeanWriteNode;
import com.reflectiveexcel.writer.node.TypedValue;
import com.reflectiveexcel.writer.style.WritableCellStyle;

public class RowWriter {

    private final WritableSheetContext sheetContext;
    private final RootTableBeanWriteNode rootNode;
    private final int headerHeight;
    private final CellWriter cellWriter;

    private int currentRow;
    private int currentColumn;

    public RowWriter(WritableSheetContext sheetContext, RootTableBeanWriteNode rootNode, CellWriter cellWriter) {
        this.sheetContext = sheetContext;
        this.rootNode = rootNode;
        this.cellWriter = cellWriter;
        this.headerHeight = rootNode.getHeight();
        this.currentRow = headerHeight;
    }

    @SuppressWarnings("unchecked")
    public void writeRow(Object row) {
        WritableCellStyle rowStyle = sheetContext.getRowStyle(row);
        currentColumn = 0;
        for (ChildBeanWriteNode tableHeader : rootNode.getChildren()) {
            List<TypedValue> leafValues = tableHeader.getLeafValues(row);
            for (TypedValue typedValue : leafValues) {
                updateWritingContext();

                Object valueToWrite = sheetContext.serialize(typedValue.getType(), typedValue.getValue());
                WritableCellStyle columnStyle = sheetContext.getColumnStyle(tableHeader.getName());
                WritableCellStyle typeStyle = sheetContext.getTypeStyle((Class<Object>) typedValue.getType(), typedValue.getValue());
                WritableCellStyle cellStyle = sheetContext.getCellStyle(typedValue.getValue());

                WritableCell cell = new WritableCell(valueToWrite, currentRow, currentColumn);
                cellWriter.writeCell(cell, List.of(rowStyle, columnStyle, typeStyle, cellStyle));
                currentColumn++;
            }
        }
        currentRow++;
    }

    private void updateWritingContext() {
        sheetContext.setCurrentTableRow(currentRow - headerHeight);
        sheetContext.setCurrentRow(currentRow + cellWriter.getOffsetRows());
        sheetContext.setCurrentTableColumn(currentColumn);
        sheetContext.setCurrentColumn(currentColumn + cellWriter.getOffsetColumns());
    }
}
