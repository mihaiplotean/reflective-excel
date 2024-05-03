package com.mihai.reader.readers;

import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ObjectReader;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.deserializer.DeserializationContext;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class SheetReader {

    private final ExcelReadingSettings settings;
    private final ReadableSheetContext sheetContext;

    public SheetReader(Sheet sheet, DeserializationContext deserializationContext, ExcelReadingSettings settings) {
        this.settings = settings;
        ReadableSheet readableSheet = new ReadableSheet(sheet);
        this.sheetContext = new ReadableSheetContext(readableSheet, deserializationContext, settings.getExceptionConsumer());
    }

    public <T> List<T> readRows(Class<T> clazz) {
        return new TableReader(sheetContext, settings).readRows(clazz);
    }

    public <T> T read(Class<T> clazz) {
        return new ObjectReader(sheetContext, settings).read(clazz);
    }
}
