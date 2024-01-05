package com.mihai;

import com.mihai.deserializer.CellDeserializer;
import com.mihai.deserializer.DefaultDeserializationContext;
import com.mihai.deserializer.DeserializationContext;
import com.mihai.workbook.WorkbookCreator;
import com.mihai.workbook.WorkbookFromFileCreator;
import com.mihai.workbook.WorkbookFromInputStreamCreator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class ReflectiveExcelReader {

    private final WorkbookCreator workbookCreator;
    private final ExcelReadingSettings settings;

    private DeserializationContext deserializationContext;

    public ReflectiveExcelReader(File file) {
        this(file, ExcelReadingSettings.DEFAULT);
    }

    public ReflectiveExcelReader(File file, ExcelReadingSettings settings) {
        this(new WorkbookFromFileCreator(file), settings);
    }

    public ReflectiveExcelReader(InputStream inputStream) {
        this(inputStream, ExcelReadingSettings.DEFAULT);
    }

    public ReflectiveExcelReader(InputStream inputStream, ExcelReadingSettings settings) {
        this(new WorkbookFromInputStreamCreator(inputStream), settings);
    }

    private ReflectiveExcelReader(WorkbookCreator workbookCreator, ExcelReadingSettings settings) {
        this.workbookCreator = workbookCreator;
        this.settings = settings;
        this.deserializationContext = new DefaultDeserializationContext();
    }

    public void setDeserializationContext(DeserializationContext deserializationContext) {
        this.deserializationContext = deserializationContext;
    }

    public <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer) {
        deserializationContext.registerDeserializer(clazz, deserializer);
    }

    public <T> List<T> readRows(Class<T> clazz) {
        try (Workbook workbook = workbookCreator.create()) {
            if (workbook.getNumberOfSheets() == 0) {
                return Collections.emptyList();
            }
            Sheet sheet = getSheet(workbook);
            return new SheetReader(sheet, deserializationContext, settings).readRows(clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Sheet getSheet(Workbook workbook) {
        String sheetName = settings.getSheetName();
        if (sheetName == null) {
            return workbook.getSheetAt(settings.getSheetIndex());
        }
        return workbook.getSheet(sheetName);
    }

    public <T> T read(Class<T> clazz) {
        try (Workbook workbook = workbookCreator.create()) {
            Sheet sheet = getSheet(workbook);
            return new SheetReader(sheet, deserializationContext, settings).read(clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
