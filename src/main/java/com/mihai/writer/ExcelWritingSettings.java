package com.mihai.writer;

import com.mihai.writer.locator.DefaultTableStartCellLocator;
import com.mihai.writer.locator.TableStartCellLocator;

public class ExcelWritingSettings {

    public static final ExcelWritingSettings DEFAULT = new ExcelWritingSettingsBuilder().create();

    private final String sheetName;
    private final ExcelFileFormat fileFormat;
    private final TableStartCellLocator tableStartCellLocator;

    private ExcelWritingSettings(ExcelWritingSettingsBuilder builder) {
        this.sheetName = builder.sheetName;
        this.fileFormat = builder.fileFormat;
        this.tableStartCellLocator = builder.tableStartCellLocator;
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

    public static ExcelWritingSettingsBuilder with() {
        return new ExcelWritingSettingsBuilder();
    }

    public static final class ExcelWritingSettingsBuilder {

        private String sheetName = "Sheet1";
        private ExcelFileFormat fileFormat = ExcelFileFormat.XLSX;
        private TableStartCellLocator tableStartCellLocator = new DefaultTableStartCellLocator();

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

        public ExcelWritingSettings create() {
            return new ExcelWritingSettings(this);
        }
    }
}
