package com.mihai.writer.writers;

import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.WritableSheetContext;
import com.mihai.writer.WritableSheet;
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
        WritableSheetContext sheetContext = new WritableSheetContext(serializationContext, cellStyleContext);
        TableWriter tableWriter = new TableWriter(sheet, sheetContext, settings);
        tableWriter.writeTable(rows, clazz, "");

        sheet.evaluateAllFormulas();
        sheet.autoResizeAllColumns();
    }

    public void write(Object object) {
        WritableSheetContext sheetContext = new WritableSheetContext(serializationContext, cellStyleContext);
        new ObjectWriter(sheet, sheetContext, settings).write(object);

        sheet.evaluateAllFormulas();
        sheet.autoResizeAllColumns();
    }
}
