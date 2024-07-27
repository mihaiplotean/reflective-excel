package com.reflectiveexcel.writer.writers;

import java.util.List;

import com.reflectiveexcel.writer.ExcelWritingSettings;
import com.reflectiveexcel.writer.WritableSheet;
import com.reflectiveexcel.writer.WritableSheetContext;
import com.reflectiveexcel.writer.serializer.SerializationContext;
import com.reflectiveexcel.writer.style.CellStyleContext;
import org.apache.poi.ss.usermodel.Sheet;

public class SheetWriter {

    private final WritableSheet sheet;

    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;
    private final ExcelWritingSettings settings;

    public SheetWriter(Sheet sheet, ExcelWritingSettings settings) {
        this.sheet = new WritableSheet(sheet);
        this.serializationContext = settings.getSerializationContext();
        this.cellStyleContext = settings.getCellStyleContext();
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
