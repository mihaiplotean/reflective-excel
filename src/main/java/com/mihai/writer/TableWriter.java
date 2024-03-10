package com.mihai.writer;

import com.mihai.reader.workbook.sheet.Bounds;
import com.mihai.writer.locator.CellLocation;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.table.WrittenTable;
import com.mihai.writer.table.WrittenTables;

import java.util.List;

public class TableWriter {

    private final WritableSheet sheet;
    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;
    private final ExcelWritingSettings settings;
    private final WritingContext writingContext;

    public TableWriter(WritableSheet sheet, SerializationContext serializationContext, CellStyleContext cellStyleContext, ExcelWritingSettings settings) {
        this(sheet, serializationContext, cellStyleContext, settings, new WritingContext(new WrittenTables()));
    }

    public TableWriter(WritableSheet sheet, SerializationContext serializationContext, CellStyleContext cellStyleContext, ExcelWritingSettings settings,
                       WritingContext writingContext) {
        this.sheet = sheet;
        this.serializationContext = serializationContext;
        this.cellStyleContext = cellStyleContext;
        this.settings = settings;
        this.writingContext = writingContext;
    }

    public <T> WrittenTable writeTable(List<T> rows, Class<T> clazz, String tableId) {
        CellWriter cellWriter = createCellWriter(tableId);

        HeaderWriter headerWriter = new HeaderWriter(cellWriter, writingContext, serializationContext, cellStyleContext);
        RootFieldNode rootFieldNode = headerWriter.writeHeaders(clazz, rows.isEmpty() ? null : rows.get(0));

        RowWriter rowWriter = new RowWriter(rootFieldNode, cellWriter, writingContext, serializationContext, cellStyleContext);

        for (T row : rows) {
            rowWriter.writeRow(row);
        }

        return new WrittenTable(tableId, new Bounds(cellWriter.getOffsetRows(),
                cellWriter.getOffsetColumns(),
                cellWriter.getOffsetRows() + rootFieldNode.getHeight() + rows.size() - 1,
                cellWriter.getOffsetColumns() + rootFieldNode.getLength() - 1
        ));
    }

    private CellWriter createCellWriter(String tableId) {
        CellWriter cellWriter = new CellWriter(sheet);

        CellLocation startingCell = settings.getTableStartCellLocator().getStartingCell(writingContext, tableId);
        cellWriter.setOffSet(startingCell.getRow(), startingCell.getColumn());

        return cellWriter;
    }
}
