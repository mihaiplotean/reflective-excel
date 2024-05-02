package com.mihai.writer.writers;

import com.mihai.reader.workbook.sheet.Bounds;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.WritableSheetContext;
import com.mihai.writer.WritableSheet;
import com.mihai.writer.locator.CellLocation;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.table.WrittenTable;
import com.mihai.writer.table.WrittenTableHeaders;

import java.util.List;

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

        RootFieldNode rootNode = new RootFieldNode(clazz, rows.isEmpty() ? null : rows.get(0));

        HeaderWriter headerWriter = new HeaderWriter(cellWriter, sheetContext);
        WrittenTableHeaders headers = headerWriter.writeHeaders(rootNode);
        sheetContext.setWritingTable(true);
        sheetContext.setCurrentTableHeaders(headers);

        RowWriter rowWriter = new RowWriter(sheetContext, rootNode, cellWriter);

        for (T row : rows) {
            rowWriter.writeRow(row);
        }

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
        cellWriter.setOffSet(startingCell.getRow(), startingCell.getColumn());

        return cellWriter;
    }
}
