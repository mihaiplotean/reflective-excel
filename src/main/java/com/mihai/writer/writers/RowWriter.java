package com.mihai.writer.writers;

import com.mihai.writer.SheetContext;
import com.mihai.writer.WritableCell;
import com.mihai.writer.node.AnnotatedFieldNode;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.node.TypedValue;
import com.mihai.writer.style.WritableCellStyle;

import java.util.List;

public class RowWriter {

    private final SheetContext sheetContext;
    private final RootFieldNode rootNode;
    private final CellWriter cellWriter;

    private int currentRow;
    private int currentColumn;

    public RowWriter(SheetContext sheetContext, RootFieldNode rootNode, CellWriter cellWriter) {
        this.sheetContext = sheetContext;
        this.rootNode = rootNode;
        this.cellWriter = cellWriter;
        this.currentRow = rootNode.getHeight();
    }

    public void writeRow(Object row) {
        WritableCellStyle rowStyle = sheetContext.getRowStyle(row);
        currentColumn = 0;
        for (AnnotatedFieldNode tableHeader : rootNode.getChildren()) {
            List<TypedValue> leafValues = tableHeader.getLeafValues(row);
            for (TypedValue typedValue : leafValues) {
                updateWritingContext();

                Object valueToWrite = sheetContext.serialize(typedValue.getType(), typedValue.getValue());
                WritableCellStyle columnStyle = sheetContext.getColumnStyle(tableHeader.getName());
                WritableCellStyle typeStyle = sheetContext.getTypeStyle(typedValue.getValue());
                WritableCellStyle cellStyle = sheetContext.getCellStyle(typedValue.getValue());

                WritableCell cell = new WritableCell(valueToWrite, currentRow, currentColumn);
                cellWriter.writeCell(cell, List.of(cellStyle, typeStyle, columnStyle, rowStyle));
                currentColumn++;
            }
        }
        currentRow++;
    }

    private void updateWritingContext() {
        sheetContext.setCurrentTableRow(currentRow);
        sheetContext.setCurrentRow(currentRow + cellWriter.getOffsetRows());
        sheetContext.setCurrentTableColumn(currentColumn);
        sheetContext.setCurrentColumn(currentColumn + cellWriter.getOffsetColumns());
    }
}
