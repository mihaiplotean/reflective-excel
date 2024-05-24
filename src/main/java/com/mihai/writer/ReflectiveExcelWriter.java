package com.mihai.writer;

import com.mihai.common.ReflectiveExcelException;
import com.mihai.common.workbook.WorkbookFromFileCreator;
import com.mihai.writer.serializer.CellSerializer;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.*;
import com.mihai.writer.writers.SheetWriter;
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

    private SerializationContext serializationContext;
    private CellStyleContext cellStyleContext;

    public ReflectiveExcelWriter(File destinationFile) {
        this(destinationFile, ExcelWritingSettings.DEFAULT);
    }

    public ReflectiveExcelWriter(File destinationFile, ExcelWritingSettings settings) {
        this.destinationFile = destinationFile;
        this.settings = settings;

        this.serializationContext = new DefaultSerializationContext();
        this.cellStyleContext = new DefaultStyleContext();
    }

    public void setSerializationContext(SerializationContext serializationContext) {
        this.serializationContext = serializationContext;
    }

    public <T> void registerSerializer(Class<T> clazz, CellSerializer<T> serializer) {
        serializationContext.registerSerializer(clazz, serializer);
    }

    public void setCellStyleContext(CellStyleContext cellStyleContext) {
        this.cellStyleContext = cellStyleContext;
    }

    public void registerTypeStyleProvider(Class<?> clazz, StyleProvider style) {
        this.cellStyleContext.setTypeStyleProvider(clazz, style);
    }

    // todo: move these options to ExcelWritingSettings?
    public void setHeaderStyleProvider(StyleProvider style) {
        this.cellStyleContext.setHeaderStyleProvider(style);
    }

    public void setCellStyleProvider(StyleProvider style) {
        this.cellStyleContext.setCellStyleProvider(style);
    }

    public void setRowStyleProvider(StyleProvider style) {
        this.cellStyleContext.setRowStyleProvider(style);
    }

    public void setColumnStyleProvider(StyleProvider style) {
        this.cellStyleContext.setColumnStyleProvider(style);
    }

    public <T> void writeRows(List<T> rows, Class<T> clazz) {
        try (Workbook workbook = createWorkbook();
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
            Sheet sheet = workbook.createSheet(settings.getSheetName());
            new SheetWriter(sheet, serializationContext, cellStyleContext, settings).writeRows(rows, clazz);
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new ReflectiveExcelException("Could not write excel file!", e);
        }
    }

    public void write(Object object) {
        try (Workbook workbook = createWorkbook();
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
            Sheet sheet = workbook.createSheet(settings.getSheetName());
            new SheetWriter(sheet, serializationContext, cellStyleContext, settings).write(object);
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new ReflectiveExcelException("Could not write excel file!", e);
        }
    }

    private Workbook createWorkbook() throws IOException {
        File templateFile = settings.getTemplateFile();
        if(templateFile != null) {
            return new WorkbookFromFileCreator(templateFile).create();
        }
        return switch (settings.getFileFormat()) {
            case XLSX -> new XSSFWorkbook();
            case XLS -> new HSSFWorkbook();
        };
    }
}
