package com.mihai.writer;

import com.mihai.writer.locator.DefaultTableStartCellLocator;
import com.mihai.writer.locator.TableStartCellLocator;
import com.mihai.writer.serializer.CellSerializer;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.DefaultStyleContext;
import com.mihai.writer.style.StyleProvider;

import java.io.File;

public class ExcelWritingSettings {

    public static final ExcelWritingSettings DEFAULT = new ExcelWritingSettingsBuilder().build();

    private final String sheetName;
    private final ExcelFileFormat fileFormat;
    private final TableStartCellLocator tableStartCellLocator;
    private final File templateFile;

    private final SerializationContext serializationContext;
    private final CellStyleContext cellStyleContext;

    private ExcelWritingSettings(ExcelWritingSettingsBuilder builder) {
        this.sheetName = builder.sheetName;
        this.fileFormat = builder.fileFormat;
        this.tableStartCellLocator = builder.tableStartCellLocator;
        this.templateFile = builder.templateFile;
        this.serializationContext = builder.serializationContext;
        this.cellStyleContext = builder.cellStyleContext;
    }

    public String getSheetName() {
        return sheetName;
    }

    public ExcelFileFormat getFileFormat() {
        return fileFormat;
    }

    public TableStartCellLocator getTableStartCellLocator() {
        return tableStartCellLocator;
    }

    public File getTemplateFile() {
        return templateFile;
    }

    public SerializationContext getSerializationContext() {
        return serializationContext;
    }

    public CellStyleContext getCellStyleContext() {
        return cellStyleContext;
    }

    public static ExcelWritingSettingsBuilder builder() {
        return new ExcelWritingSettingsBuilder();
    }

    public static final class ExcelWritingSettingsBuilder {

        private String sheetName = "Sheet1";
        private ExcelFileFormat fileFormat = ExcelFileFormat.XLSX;
        private TableStartCellLocator tableStartCellLocator = new DefaultTableStartCellLocator();
        private File templateFile;

        private SerializationContext serializationContext = new DefaultSerializationContext();
        private CellStyleContext cellStyleContext = new DefaultStyleContext();

        public ExcelWritingSettingsBuilder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public ExcelWritingSettingsBuilder fileFormat(ExcelFileFormat fileFormat) {
            this.fileFormat = fileFormat;
            return this;
        }

        public ExcelWritingSettingsBuilder tableStartCellLocator(TableStartCellLocator tableStartCellLocator) {
            this.tableStartCellLocator = tableStartCellLocator;
            return this;
        }

        public ExcelWritingSettingsBuilder templateFile(File templateFile) {
            this.templateFile = templateFile;
            return this;
        }

        public ExcelWritingSettingsBuilder serializationContext(SerializationContext serializationContext) {
            this.serializationContext = serializationContext;
            return this;
        }

        public <T> ExcelWritingSettingsBuilder registerSerializer(Class<T> clazz, CellSerializer<T> serializer) {
            serializationContext.registerSerializer(clazz, serializer);
            return this;
        }

        public ExcelWritingSettingsBuilder cellStyleContext(CellStyleContext cellStyleContext) {
            this.cellStyleContext = cellStyleContext;
            return this;
        }

        public ExcelWritingSettingsBuilder registerTypeStyleProvider(Class<?> clazz, StyleProvider style) {
            this.cellStyleContext.setTypeStyleProvider(clazz, style);
            return this;
        }

        public ExcelWritingSettingsBuilder headerStyleProvider(StyleProvider style) {
            this.cellStyleContext.setHeaderStyleProvider(style);
            return this;
        }

        public ExcelWritingSettingsBuilder cellStyleProvider(StyleProvider style) {
            this.cellStyleContext.setCellStyleProvider(style);
            return this;
        }

        public ExcelWritingSettingsBuilder rowStyleProvider(StyleProvider style) {
            this.cellStyleContext.setRowStyleProvider(style);
            return this;
        }

        public ExcelWritingSettingsBuilder columnStyleProvider(StyleProvider style) {
            this.cellStyleContext.setColumnStyleProvider(style);
            return this;
        }

        public ExcelWritingSettings build() {
            return new ExcelWritingSettings(this);
        }
    }
}
