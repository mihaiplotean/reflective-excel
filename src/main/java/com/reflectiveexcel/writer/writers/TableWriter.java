package com.reflectiveexcel.writer.writers;

import java.util.List;
import java.util.Map;

import com.reflectiveexcel.core.workbook.Bounds;
import com.reflectiveexcel.core.workbook.CellLocation;
import com.reflectiveexcel.writer.ExcelWritingSettings;
import com.reflectiveexcel.writer.WritableSheet;
import com.reflectiveexcel.writer.WritableSheetContext;
import com.reflectiveexcel.writer.node.ChildBeanWriteNode;
import com.reflectiveexcel.writer.node.RootTableBeanWriteNode;
import com.reflectiveexcel.writer.sizing.ColumnSizeSetter;
import com.reflectiveexcel.writer.table.WrittenTable;
import com.reflectiveexcel.writer.table.WrittenTableHeaders;

public class TableWriter {

    private final WritableSheet sheet;
    private final WritableSheetContext sheetContext;
    private final ExcelWritingSettings settings;

    public TableWriter(WritableSheet sheet, WritableSheetContext sheetContext, ExcelWritingSettings settings) {
        this.sheet = sheet;
        this.sheetContext = sheetContext;
        this.settings = settings;
    }

    public <T> WrittenTable writeTable(List<T> rows, Class<T> clazz, String tableId) {
        CellWriter cellWriter = createCellWriter(tableId);

        RootTableBeanWriteNode rootNode = new RootTableBeanWriteNode(clazz, rows.isEmpty() ? null : rows.get(0));

        HeaderWriter headerWriter = new HeaderWriter(cellWriter, sheetContext);
        WrittenTableHeaders headers = headerWriter.writeHeaders(rootNode);
        sheetContext.setWritingTable(true);
        sheetContext.setCurrentTableHeaders(headers);

        RowWriter rowWriter = new RowWriter(sheetContext, rootNode, cellWriter);

        for (T row : rows) {
            rowWriter.writeRow(row);
        }
        setSetColumnSizes(headerWriter.getLeafNodeToColumnIndexMap());

        sheetContext.setWritingTable(false);
        return new WrittenTable(tableId, headers, new Bounds(cellWriter.getOffsetRows(),
                                                             cellWriter.getOffsetColumns(),
                                                             cellWriter.getOffsetRows() + rootNode.getHeight() + rows.size() - 1,
                                                             cellWriter.getOffsetColumns() + rootNode.getLength() - 1
        ));
    }

    private CellWriter createCellWriter(String tableId) {
        CellWriter cellWriter = new CellWriter(sheet);

        CellLocation startingCell = settings.getTableStartCellLocator().getStartingCell(sheetContext.getWritingContext(), tableId);
        cellWriter.setOffSet(startingCell.row(), startingCell.column());

        return cellWriter;
    }

    private void setSetColumnSizes(Map<ChildBeanWriteNode, Integer> leafNodeToColumnIndexMap) {
        new ColumnSizeSetter(sheet).setColumnSizes(leafNodeToColumnIndexMap);
    }
}
