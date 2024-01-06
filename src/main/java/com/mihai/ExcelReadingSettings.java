package com.mihai;

import com.mihai.exception.BadInputExceptionConsumer;
import com.mihai.detector.ColumnDetector;
import com.mihai.detector.ColumnDetectors;
import com.mihai.detector.RowDetector;
import com.mihai.detector.RowDetectors;
import org.apache.poi.ss.util.CellReference;

public class ExcelReadingSettings {

    public static final ExcelReadingSettings DEFAULT = new ExcelReadingSettingsBuilder().create();

    private final String sheetName;
    private final int sheetIndex;

    private final RowDetector endRowDetector;
    private final RowDetector headerRowDetector;

    private final RowDetector skipRowDetector;
    private final ColumnDetector skipColumnDetector;

    private final BadInputExceptionConsumer exceptionConsumer;

    private ExcelReadingSettings(ExcelReadingSettingsBuilder builder) {
        sheetName = builder.sheetName;
        sheetIndex = builder.sheetIndex;

        skipColumnDetector = builder.skipColumnDetector;

        endRowDetector = builder.endRowDetector;
        headerRowDetector = builder.headerRowDetector;
        skipRowDetector = builder.skipRowDetector;

        exceptionConsumer = builder.exceptionConsumer;
    }

    public String getSheetName() {
        return sheetName;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public ColumnDetector getSkipColumnDetector() {
        return skipColumnDetector;
    }

    public RowDetector getEndRowDetector() {
        return endRowDetector;
    }

    public RowDetector getHeaderRowDetector() {
        return headerRowDetector;
    }

    public RowDetector getSkipRowDetector() {
        return skipRowDetector;
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

        private RowDetector endRowDetector = RowDetectors.allCellsEmpty();
        private RowDetector headerRowDetector = RowDetectors.isFirstRow();
        private RowDetector skipRowDetector = RowDetectors.never();
        private ColumnDetector skipColumnDetector = ColumnDetectors.never();

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

        public ExcelReadingSettingsBuilder headerStartCellReference(String headerStartCellReference) {
            CellReference reference = new CellReference(headerStartCellReference);
            this.headerRowDetector = RowDetectors.isRowWithNumber(reference.getRow());
            this.skipColumnDetector = ColumnDetectors.isBefore(reference.getCol());
            return this;
        }

        public ExcelReadingSettingsBuilder endRowDetector(RowDetector endRowDetector) {
            this.endRowDetector = endRowDetector;
            return this;
        }

        public ExcelReadingSettingsBuilder headerRowDetector(RowDetector headerRowDetector) {
            this.headerRowDetector = headerRowDetector;
            return this;
        }

        public ExcelReadingSettingsBuilder skipRowDetector(RowDetector skipRowDetector) {
            this.skipRowDetector = skipRowDetector;
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
