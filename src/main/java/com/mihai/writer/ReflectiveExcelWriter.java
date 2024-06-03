package com.mihai.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.mihai.core.ReflectiveExcelException;
import com.mihai.core.workbook.WorkbookFromFileCreator;
import com.mihai.writer.writers.SheetWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReflectiveExcelWriter {

    private final File destinationFile;
    private final ExcelWritingSettings settings;

    public ReflectiveExcelWriter(File destinationFile) {
        this(destinationFile, ExcelWritingSettings.DEFAULT);
    }

    public ReflectiveExcelWriter(File destinationFile, ExcelWritingSettings settings) {
        this.destinationFile = destinationFile;
        this.settings = settings;
    }

    public <T> void writeRows(List<T> rows, Class<T> clazz) {
        try (Workbook workbook = createWorkbook();
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
            Sheet sheet = getOrCreateSheet(workbook);
            new SheetWriter(sheet, settings).writeRows(rows, clazz);
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new ReflectiveExcelException("Could not write excel file!", e);
        }
    }

    public void write(Object object) {
        try (Workbook workbook = createWorkbook();
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
            Sheet sheet = getOrCreateSheet(workbook);
            new SheetWriter(sheet, settings).write(object);
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new ReflectiveExcelException("Could not write excel file!", e);
        }
    }

    private Workbook createWorkbook() throws IOException {
        File templateFile = settings.getTemplateFile();
        if (templateFile != null) {
            return new WorkbookFromFileCreator(templateFile).create();
        }
        return switch (settings.getFileFormat()) {
            case XLSX -> new XSSFWorkbook();
            case XLS -> new HSSFWorkbook();
        };
    }

    private Sheet getOrCreateSheet(Workbook workbook) {
        String sheetName = settings.getSheetName();
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            return workbook.createSheet(sheetName);
        }
        return sheet;
    }
}
