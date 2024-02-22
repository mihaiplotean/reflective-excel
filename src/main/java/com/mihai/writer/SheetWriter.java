package com.mihai.writer;

import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.DefaultStyleContext;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class SheetWriter {

    private final WritableSheet sheet;
    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;

    public SheetWriter(Sheet sheet) {
        this.sheet = new WritableSheet(sheet);
        this.serializationContext = new DefaultSerializationContext();
        this.cellStyleContext = new DefaultStyleContext();
    }

    public <T> void writeRows(List<T> rows, Class<T> clazz) {
        CellWriter cellWriter = new CellWriter(sheet);

        HeaderWriter headerWriter = new HeaderWriter(cellWriter, serializationContext, cellStyleContext);
        RootFieldNode rootFieldNode = headerWriter.writeHeaders(clazz, rows.isEmpty() ? null : rows.get(0));

        RowWriter rowWriter = new RowWriter(rootFieldNode, cellWriter, serializationContext, cellStyleContext);

        for (T row : rows) {
            rowWriter.writeRow(row);
        }

        sheet.evaluateAllFormulas();
        sheet.autoResizeAllColumns();
    }
}
