package com.mihai.writer;

import com.mihai.reader.workbook.sheet.Bounds;
import com.mihai.writer.locator.CellLocation;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.table.CellWritingContext;
import com.mihai.writer.table.TableWritingContext;
import com.mihai.writer.table.WrittenTable;
import com.mihai.writer.table.WrittenTableHeaders;

import java.util.List;

public class TableWriter {

    private final WritableSheet sheet;
    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;
    private final ExcelWritingSettings settings;
    private final WritingContext writingContext;

    private final TableWritingContext tableContext;
    private final CellWritingContext cellWritingContext;

    public TableWriter(WritableSheet sheet, SerializationContext serializationContext, CellStyleContext cellStyleContext, ExcelWritingSettings settings,
                       TableWritingContext tableContext, WritingContext writingContext, CellWritingContext cellWritingContext) {
        this.sheet = sheet;
        this.serializationContext = serializationContext;
        this.cellStyleContext = cellStyleContext;
        this.settings = settings;
        this.writingContext = writingContext;
        this.tableContext = tableContext;
        this.cellWritingContext = cellWritingContext;
    }

    public <T> WrittenTable writeTable(List<T> rows, Class<T> clazz, String tableId) {
        CellWriter cellWriter = createCellWriter(tableId);

        RootFieldNode rootNode = new RootFieldNode(clazz, rows.isEmpty() ? null : rows.get(0));

        HeaderWriter headerWriter = new HeaderWriter(cellWriter, writingContext, serializationContext, cellStyleContext, cellWritingContext);
        WrittenTableHeaders headers = headerWriter.writeHeaders(rootNode);
        tableContext.setWritingTable(true);
        tableContext.setCurrentTableHeaders(headers);

        RowWriter rowWriter = new RowWriter(rootNode, cellWriter, writingContext, serializationContext, cellStyleContext, tableContext);

        for (T row : rows) {
            rowWriter.writeRow(row);
        }

        tableContext.setWritingTable(false);
        return new WrittenTable(tableId, headers, new Bounds(cellWriter.getOffsetRows(),
                cellWriter.getOffsetColumns(),
                cellWriter.getOffsetRows() + rootNode.getHeight() + rows.size() - 1,
                cellWriter.getOffsetColumns() + rootNode.getLength() - 1
        ));
    }

    private CellWriter createCellWriter(String tableId) {
        CellWriter cellWriter = new CellWriter(sheet);

        CellLocation startingCell = settings.getTableStartCellLocator().getStartingCell(writingContext, tableId);
        cellWriter.setOffSet(startingCell.getRow(), startingCell.getColumn());

        return cellWriter;
    }
}
