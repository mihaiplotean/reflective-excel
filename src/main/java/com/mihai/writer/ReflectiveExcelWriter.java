package com.mihai.writer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
            Sheet sheet = workbook.createSheet(settings.getSheetName());
            new SheetWriter(sheet).writeRows(rows, clazz);
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Workbook createWorkbook() {
        switch (settings.getFileFormat()) {
            case XLSX -> {
                return new XSSFWorkbook();
            }
            case XLS -> {
                return new HSSFWorkbook();
            }
        }
        throw new IllegalArgumentException("Unsupported file format!");
    }
}
