package com.mihai.reader;

import com.mihai.common.ReflectiveExcelException;
import com.mihai.reader.readers.SheetReader;
import com.mihai.common.workbook.WorkbookCreator;
import com.mihai.common.workbook.WorkbookFromFileCreator;
import com.mihai.reader.deserializer.CellDeserializer;
import com.mihai.reader.deserializer.DefaultDeserializationContext;
import com.mihai.reader.deserializer.DeserializationContext;
import com.mihai.common.workbook.WorkbookFromInputStreamCreator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class ReflectiveExcelReader {

    private final WorkbookCreator workbookCreator;

    private DeserializationContext deserializationContext;

    public ReflectiveExcelReader(File file) {
        this(new WorkbookFromFileCreator(file));
    }

    public ReflectiveExcelReader(InputStream inputStream) {
        this(new WorkbookFromInputStreamCreator(inputStream));
    }

    private ReflectiveExcelReader(WorkbookCreator workbookCreator) {
        this.workbookCreator = workbookCreator;
        this.deserializationContext = new DefaultDeserializationContext();
    }

    public void setDeserializationContext(DeserializationContext deserializationContext) {
        this.deserializationContext = deserializationContext;
    }

    public <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer) {
        deserializationContext.registerDeserializer(clazz, deserializer);
    }

    public <T> List<T> readRows(Class<T> clazz) {
        return readRows(clazz, ExcelReadingSettings.DEFAULT);
    }

    public <T> List<T> readRows(Class<T> clazz, ExcelReadingSettings settings) {
        try (Workbook workbook = workbookCreator.create()) {
            if (workbook.getNumberOfSheets() == 0) {
                return Collections.emptyList();
            }
            Sheet sheet = getSheet(workbook, settings);
            return new SheetReader(sheet, deserializationContext, settings).readRows(clazz);
        } catch (IOException e) {
            throw new ReflectiveExcelException("Could not read excel file!", e);
        }
    }

    private Sheet getSheet(Workbook workbook, ExcelReadingSettings settings) {
        String sheetName = settings.getSheetName();
        if (sheetName == null) {
            int numberOfSheets = workbook.getNumberOfSheets();
            int sheetIndex = settings.getSheetIndex();
            return workbook.getSheetAt(translateIndex(sheetIndex, numberOfSheets));
        }
        return workbook.getSheet(sheetName);
    }

    private int translateIndex(int index, int total) {
        if (index >= 0) {
            return index;
        }
        return total + index;
    }

    public <T> T read(Class<T> clazz) {
        return read(clazz, ExcelReadingSettings.DEFAULT);
    }

    public <T> T read(Class<T> clazz, ExcelReadingSettings settings) {
        try (Workbook workbook = workbookCreator.create()) {
            Sheet sheet = getSheet(workbook, settings);
            return new SheetReader(sheet, deserializationContext, settings).read(clazz);
        } catch (IOException e) {
            throw new ReflectiveExcelException("Could not read excel file!", e);
        }
    }
}
