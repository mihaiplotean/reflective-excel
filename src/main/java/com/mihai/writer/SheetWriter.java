package com.mihai.writer;

import com.mihai.writer.node.AnnotatedFieldNodeInterface;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.node.TypedValue;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.serializer.WritableCellStyle;
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
        RootFieldNode rootNode = writeHeaders(clazz, rows.isEmpty() ? null : rows.get(0));

        int currentColumn = 0;
        int currentRow = rootNode.getHeight();
        for (T row : rows) {
            for (AnnotatedFieldNodeInterface tableHeader : rootNode.getChildren()) {
                List<TypedValue> leafValues = tableHeader.getLeafValues(row);
                for (TypedValue typedValue : leafValues) {
                    writeCell(typedValue.getType(), typedValue.getValue(), currentRow, currentColumn, currentRow, currentColumn);
                    currentColumn++;
                }
            }
            currentColumn = 0;
            currentRow++;
        }

        sheet.evaluateAllFormulas();
        sheet.autoResizeAllColumns();
    }

    private RootFieldNode writeHeaders(Class<?> clazz, Object firstRow) {
        RootFieldNode rootNode = new RootFieldNode(clazz, firstRow);
        int currentColumn = 0;
        for (AnnotatedFieldNodeInterface child : rootNode.getChildren()) {
            writeHeader(child, rootNode.getHeight() + 1, 0, currentColumn);
            currentColumn += child.getLength();
        }
        return rootNode;
    }

    private void writeHeader(AnnotatedFieldNodeInterface node, int headerHeight, int currentRow, int currentColumn) {
        if (node.getHeight() > 0 && node.getLength() > 0) {
            writeCell(node.getType(), node.getName(),
                    currentRow,
                    currentColumn,
                    currentRow + headerHeight - node.getHeight() - 1,
                    currentColumn + node.getLength() - 1
            );
            currentRow += headerHeight - node.getHeight();
        }
        for (AnnotatedFieldNodeInterface child : node.getChildren()) {
            writeHeader(child, headerHeight - 1, currentRow, currentColumn);
            currentColumn += child.getLength();
        }
    }

    private void writeCell(Class<?> type, Object value, int startRow, int startColumn, int endRow, int endColumn) {
        Object valueToWrite = serializationContext.serialize((Class<Object>) type, value);
        WritableCellStyle style = cellStyleContext.getStyle(null, (Class<Object>) type, value);
        sheet.writeCell(new WritableCell(valueToWrite, style, startRow, startColumn, endRow, endColumn));
    }
}
