package com.mihai.writer;

import com.mihai.writer.locator.DefaultTableStartCellLocator;
import com.mihai.writer.locator.TableStartCellLocator;

import java.io.File;

public class ExcelWritingSettings {

    public static final ExcelWritingSettings DEFAULT = new ExcelWritingSettingsBuilder().create();

    private final String sheetName;
    private final ExcelFileFormat fileFormat;
    private final TableStartCellLocator tableStartCellLocator;
    private final File templateFile;

    private ExcelWritingSettings(ExcelWritingSettingsBuilder builder) {
        this.sheetName = builder.sheetName;
        this.fileFormat = builder.fileFormat;
        this.tableStartCellLocator = builder.tableStartCellLocator;
        this.templateFile = builder.templateFile;
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

    public static ExcelWritingSettingsBuilder with() {
        return new ExcelWritingSettingsBuilder();
    }

    public static final class ExcelWritingSettingsBuilder {

        private String sheetName = "Sheet1";
        private ExcelFileFormat fileFormat = ExcelFileFormat.XLSX;
        private TableStartCellLocator tableStartCellLocator = new DefaultTableStartCellLocator();
        private File templateFile;

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

        public ExcelWritingSettings create() {
            return new ExcelWritingSettings(this);
        }
    }
}
