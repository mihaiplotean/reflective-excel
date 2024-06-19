package com.mihai.writer;

import java.io.File;

import com.mihai.writer.locator.DefaultTableStartCellLocator;
import com.mihai.writer.locator.TableStartCellLocator;
import com.mihai.writer.serializer.CellSerializer;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.DefaultStyleContext;
import com.mihai.writer.style.StyleProvider;

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

        /**
         * Specifies the name of the sheet to which the table(s) will be written.
         *
         * @param sheetName name of the sheet.
         */
        public ExcelWritingSettingsBuilder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        /**
         * Specifies the format of the Excel file that will be written.
         *
         * @param fileFormat Excel format.
         */
        public ExcelWritingSettingsBuilder fileFormat(ExcelFileFormat fileFormat) {
            this.fileFormat = fileFormat;
            return this;
        }

        /**
         * Used to specify the cell at which the table starts. By default, the first table starts at cell "A1".
         *
         * @param tableStartCellLocator table start cell locator.
         */
        public ExcelWritingSettingsBuilder tableStartCellLocator(TableStartCellLocator tableStartCellLocator) {
            this.tableStartCellLocator = tableStartCellLocator;
            return this;
        }

        /**
         * Instead of creating a new Excel file to which the table(s) are written, this method can be used
         * to write the table(s) to a template file. The setting {@link #fileFormat(ExcelFileFormat)} is ignored in this case.
         * This could be useful, if, for example, there should be some images or other fixed information on top of the written table(s).
         *
         * @param templateFile the file to which the table(s) should be written.
         */
        public ExcelWritingSettingsBuilder templateFile(File templateFile) {
            this.templateFile = templateFile;
            return this;
        }

        /**
         * Allows to provide a custom serialization context, which specifies how a type is converted to
         * a cell value.
         *
         * @param serializationContext serialization context to be used during writing.
         */
        public ExcelWritingSettingsBuilder serializationContext(SerializationContext serializationContext) {
            this.serializationContext = serializationContext;
            return this;
        }

        public <T> ExcelWritingSettingsBuilder registerSerializer(Class<T> clazz, CellSerializer<T> serializer) {
            serializationContext.registerSerializer(clazz, serializer);
            return this;
        }

        /**
         * Allows to provide a custom cell style context, which applies styling to the written cells.
         *
         * @param cellStyleContext cell style context to be used during writing.
         */
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
