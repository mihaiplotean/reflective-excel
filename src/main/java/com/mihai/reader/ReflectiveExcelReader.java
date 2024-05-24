package com.mihai.reader;

import com.mihai.common.ReflectiveExcelException;
import com.mihai.reader.readers.SheetReader;
import com.mihai.common.workbook.WorkbookCreator;
import com.mihai.common.workbook.WorkbookFromFileCreator;
import com.mihai.reader.deserializer.CellDeserializer;
import com.mihai.reader.deserializer.DefaultDeserializationContext;
import com.mihai.reader.deserializer.DeserializationContext;
import com.mihai.common.workbook.WorkbookFromInputStreamCreator;
import com.mihai.writer.ExcelWritingSettings;
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

    public ReflectiveExcelReader(File file) {
        this(file, ExcelReadingSettings.DEFAULT);
    }

    public ReflectiveExcelReader(InputStream inputStream) {
        this(inputStream, ExcelReadingSettings.DEFAULT);
    }

    public ReflectiveExcelReader(File file, ExcelReadingSettings settings) {
        this(new WorkbookFromFileCreator(file), settings);
    }

    public ReflectiveExcelReader(InputStream inputStream, ExcelReadingSettings settings) {
        this(new WorkbookFromInputStreamCreator(inputStream), settings);
    }

    private ReflectiveExcelReader(WorkbookCreator workbookCreator, ExcelReadingSettings settings) {
        this.workbookCreator = workbookCreator;
        this.settings = settings;
    }

    public <T> List<T> readRows(Class<T> clazz) {
        try (Workbook workbook = workbookCreator.create()) {
            if (workbook.getNumberOfSheets() == 0) {
                return Collections.emptyList();
            }
            Sheet sheet = getSheet(workbook, settings);
            return new SheetReader(sheet, settings).readRows(clazz);
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
        try (Workbook workbook = workbookCreator.create()) {
            Sheet sheet = getSheet(workbook, settings);
            return new SheetReader(sheet, settings).read(clazz);
        } catch (IOException e) {
            throw new ReflectiveExcelException("Could not read excel file!", e);
        }
    }
}
