package com.mihai.reader;

import com.mihai.reader.detector.*;
import com.mihai.reader.exception.BadInputExceptionConsumer;

public class ExcelReadingSettings {

    public static final ExcelReadingSettings DEFAULT = new ExcelReadingSettingsBuilder().create();

    private final String sheetName;
    private final int sheetIndex;

    private final TableRowColumnDetector rowColumnDetector;

    private final BadInputExceptionConsumer exceptionConsumer;

    private ExcelReadingSettings(ExcelReadingSettingsBuilder builder) {
        sheetName = builder.sheetName;
        sheetIndex = builder.sheetIndex;

        rowColumnDetector = builder.rowColumnDetector;

        exceptionConsumer = builder.exceptionConsumer;
    }

    public String getSheetName() {
        return sheetName;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public TableRowColumnDetector getRowColumnDetector() {
        return rowColumnDetector;
    }

    public BadInputExceptionConsumer getExceptionConsumer() {
        return exceptionConsumer;
    }

    public static ExcelReadingSettingsBuilder with() {
        return new ExcelReadingSettingsBuilder();
    }

    public static final class ExcelReadingSettingsBuilder {

        private String sheetName;
        private int sheetIndex;

        private TableRowColumnDetector rowColumnDetector = new SimpleRowColumnDetector("A1");

        private BadInputExceptionConsumer exceptionConsumer = (row, exception) -> {
            throw exception;
        };

        public ExcelReadingSettingsBuilder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public ExcelReadingSettingsBuilder sheetIndex(int sheetIndex) {
            this.sheetIndex = sheetIndex;
            return this;
        }

        public ExcelReadingSettingsBuilder headerStartCellReference(String cellReference) {
            return rowColumnDetector(new SimpleRowColumnDetector(cellReference));
        }

        public ExcelReadingSettingsBuilder autoRowColumnDetector() {
            return rowColumnDetector(new AutoRowColumnDetector());
        }

        public ExcelReadingSettingsBuilder rowColumnDetector(TableRowColumnDetector rowColumnDetector) {
            this.rowColumnDetector = rowColumnDetector;
            return this;
        }

        public ExcelReadingSettingsBuilder onException(BadInputExceptionConsumer exceptionConsumer) {
            this.exceptionConsumer = exceptionConsumer;
            return this;
        }

        public ExcelReadingSettings create() {
            return new ExcelReadingSettings(this);
        }
    }
}
