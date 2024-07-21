package com.mihai.reader.readers;

import java.util.List;

import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.ss.usermodel.Sheet;

public class SheetReader {

    private final ExcelReadingSettings settings;
    private final ReadableSheetContext sheetContext;

    public SheetReader(Sheet sheet, ExcelReadingSettings settings) {
        this.settings = settings;
        ReadableSheet readableSheet = new ReadableSheet(sheet);
        this.sheetContext = new ReadableSheetContext(readableSheet, settings);
    }

    public <T> List<T> readRows(Class<T> clazz) {
        return new TableReader(sheetContext, settings).readRows(clazz);
    }

    public <T> T read(Class<T> clazz) {
        return new ObjectReader(sheetContext, settings).read(clazz);
    }
}
