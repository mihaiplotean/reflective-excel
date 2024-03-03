package com.mihai.writer;

import com.mihai.FieldAnalyzer;
import com.mihai.reader.field.AnnotatedField;
import com.mihai.reader.field.CellValueField;
import com.mihai.writer.node.RootFieldNode;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.DefaultStyleContext;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

public class SheetWriter {

    private final WritableSheet sheet;
    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;

    public SheetWriter(Sheet sheet, SerializationContext serializationContext, CellStyleContext cellStyleContext) {
        this.sheet = new WritableSheet(sheet);
        this.serializationContext = serializationContext;
        this.cellStyleContext = cellStyleContext;
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

    public <T> void write(Class<T> clazz) {
        FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(clazz);

        List<AnnotatedField> fields = new ArrayList<>();
        fields.addAll(fieldAnalyzer.getExcelCellValueFields());
        fields.addAll(fieldAnalyzer.getExcelPropertyFields());
        fields.addAll(fieldAnalyzer.getExcelRowsFields());

        for (CellValueField field : fieldAnalyzer.getExcelCellValueFields()) {

        }
    }
}
