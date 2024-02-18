package com.mihai.writer;

import com.mihai.ExcelReadingSettings;

public class ExcelWritingSettings {

    public static final ExcelWritingSettings DEFAULT = new ExcelWritingSettingsBuilder().create();

    private final String sheetName;
    private final ExcelFileFormat fileFormat;

    public ExcelWritingSettings(String sheetName, ExcelFileFormat fileFormat) {
        this.sheetName = sheetName;
        this.fileFormat = fileFormat;
    }

    private ExcelWritingSettings(ExcelWritingSettingsBuilder builder) {
        sheetName = builder.sheetName;
        fileFormat = builder.fileFormat;
    }

    public String getSheetName() {
        return sheetName;
    }

    public ExcelFileFormat getFileFormat() {
        return fileFormat;
    }

    public static ExcelWritingSettingsBuilder with() {
        return new ExcelWritingSettingsBuilder();
    }

    public static final class ExcelWritingSettingsBuilder {

        private String sheetName = "Sheet1";
        private ExcelFileFormat fileFormat = ExcelFileFormat.XLSX;

        public ExcelWritingSettingsBuilder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public ExcelWritingSettingsBuilder fileFormat(ExcelFileFormat fileFormat) {
            this.fileFormat = fileFormat;
            return this;
        }

        public ExcelWritingSettings create() {
            return new ExcelWritingSettings(this);
        }
    }
}
