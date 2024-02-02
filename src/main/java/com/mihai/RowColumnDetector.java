package com.mihai;

import com.mihai.detector.ColumnDetector;
import com.mihai.detector.RowDetector;
import com.mihai.workbook.sheet.PropertyCell;
import com.mihai.workbook.sheet.RowCells;

public class RowColumnDetector {

    private final ReadingContext context;

    private final RowDetector lastRowDetector;
    private final RowDetector headerRowDetector;

    private final RowDetector skipRowDetector;
    private final ColumnDetector headerStartColumnDetector;
    private final ColumnDetector headerLastColumnDetector;

    private RowColumnDetector(RowColumnDetectorBuilder builder) {
        this.context = builder.context;
        this.lastRowDetector = builder.lastRowDetector;
        this.headerRowDetector = builder.headerRowDetector;
        this.skipRowDetector = builder.skipRowDetector;
        this.headerStartColumnDetector = builder.headerStartColumnDetector;
        this.headerLastColumnDetector = builder.headerLastColumnDetector;
    }

    public boolean isLastRow(RowCells row) {
        return lastRowDetector.test(context, row);
    }

    public boolean isHeaderRow(RowCells row) {
        return headerRowDetector.test(context, row);
    }

    public boolean shouldSkipRow(RowCells row) {
        return skipRowDetector.test(context, row);
    }

    public boolean isHeaderStartColumn(PropertyCell cell) {
        return headerStartColumnDetector.test(context, cell);
    }

    public boolean isHeaderLastColumn(PropertyCell cell) {
        return headerLastColumnDetector.test(context, cell);
    }

    public static RowColumnDetectorBuilder builder(ReadingContext context) {
        return new RowColumnDetectorBuilder(context);
    }

    public static final class RowColumnDetectorBuilder {

        private final ReadingContext context;

        private ColumnDetector headerStartColumnDetector;
        private ColumnDetector headerLastColumnDetector;
        private RowDetector lastRowDetector;
        private RowDetector headerRowDetector;
        private RowDetector skipRowDetector;

        public RowColumnDetectorBuilder(ReadingContext context) {
            this.context = context;
        }

        public RowColumnDetectorBuilder lastRowDetector(RowDetector lastRowDetector) {
            this.lastRowDetector = lastRowDetector;
            return this;
        }

        public RowColumnDetectorBuilder headerRowDetector(RowDetector headerRowDetector) {
            this.headerRowDetector = headerRowDetector;
            return this;
        }

        public RowColumnDetectorBuilder skipRowDetector(RowDetector skipRowDetector) {
            this.skipRowDetector = skipRowDetector;
            return this;
        }

        public RowColumnDetectorBuilder headerStartColumnDetector(ColumnDetector headerStartColumnDetector) {
            this.headerStartColumnDetector = headerStartColumnDetector;
            return this;
        }

        public RowColumnDetectorBuilder headerLastColumnDetector(ColumnDetector headerLastColumnDetector) {
            this.headerLastColumnDetector = headerLastColumnDetector;
            return this;
        }

        public RowColumnDetector build() {
            return new RowColumnDetector(this);
        }
    }
}
