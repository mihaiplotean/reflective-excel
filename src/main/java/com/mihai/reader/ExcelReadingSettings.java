package com.mihai.reader;

import com.mihai.reader.detector.*;
import com.mihai.reader.exception.BadInputExceptionConsumer;
import org.apache.poi.ss.util.CellReference;

public class ExcelReadingSettings {

    public static final ExcelReadingSettings DEFAULT = new ExcelReadingSettingsBuilder().create();

    private final String sheetName;
    private final int sheetIndex;

    private final RowColumnDetector2 rowColumnDetector2;

    private final BadInputExceptionConsumer exceptionConsumer;

    private ExcelReadingSettings(ExcelReadingSettingsBuilder builder) {
        sheetName = builder.sheetName;
        sheetIndex = builder.sheetIndex;

        rowColumnDetector2 = builder.rowColumnDetector2;

        exceptionConsumer = builder.exceptionConsumer;
    }

    public String getSheetName() {
        return sheetName;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public RowColumnDetector2 getRowColumnDetector2() {
        return rowColumnDetector2;
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

        private RowColumnDetector2 rowColumnDetector2 = new SimpleRowColumnDetector("A1");

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
            return rowColumnDetector2(new SimpleRowColumnDetector(cellReference));
        }

        public ExcelReadingSettingsBuilder rowColumnDetector2(RowColumnDetector2 rowColumnDetector2) {
            this.rowColumnDetector2 = rowColumnDetector2;
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
