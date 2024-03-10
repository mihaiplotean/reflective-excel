package com.mihai.writer;

import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class SheetWriter {

    private final WritableSheet sheet;

    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;
    private final ExcelWritingSettings settings;

    public SheetWriter(Sheet sheet, SerializationContext serializationContext, CellStyleContext cellStyleContext,
                       ExcelWritingSettings settings) {
        this.sheet = new WritableSheet(sheet);
        this.serializationContext = serializationContext;
        this.cellStyleContext = cellStyleContext;
        this.settings = settings;
    }

    public <T> void writeRows(List<T> rows, Class<T> clazz) {
        TableWriter tableWriter = new TableWriter(sheet, serializationContext, cellStyleContext, settings);
        tableWriter.writeTable(rows, clazz, "");

        sheet.evaluateAllFormulas();
        sheet.autoResizeAllColumns();
    }

    public void write(Object object) {
        new ObjectWriter(sheet, cellStyleContext, serializationContext, settings).write(object);

        sheet.evaluateAllFormulas();
        sheet.autoResizeAllColumns();
    }
}
