package com.mihai;

import com.mihai.detector.ColumnDetector;
import com.mihai.detector.RowDetector;
import com.mihai.workbook.sheet.PropertyCell;
import com.mihai.workbook.sheet.RowCells;

public class RowColumnDetector {

    private final ReadingContext context;

    private final RowDetector endRowDetector;
    private final RowDetector headerRowDetector;

    private final RowDetector skipRowDetector;
    private final ColumnDetector skipColumnDetector;

    private RowColumnDetector(RowColumnDetectorBuilder builder) {
        context = builder.context;
        endRowDetector = builder.endRowDetector;
        headerRowDetector = builder.headerRowDetector;
        skipRowDetector = builder.skipRowDetector;
        skipColumnDetector = builder.skipColumnDetector;
    }

    public boolean isEndRow(RowCells row) {
        return endRowDetector.test(context, row);
    }

    public boolean isHeaderRow(RowCells row) {
        return headerRowDetector.test(context, row);
    }

    public boolean shouldSkipRow(RowCells row) {
        return skipRowDetector.test(context, row);
    }

    public boolean shouldSkipColumn(PropertyCell cell) {
        return skipColumnDetector.test(context, cell);
    }

    public static RowColumnDetectorBuilder builder(ReadingContext context) {
        return new RowColumnDetectorBuilder(context);
    }

    public static final class RowColumnDetectorBuilder {

        private final ReadingContext context;

        private ColumnDetector skipColumnDetector;
        private RowDetector endRowDetector;
        private RowDetector headerRowDetector;
        private RowDetector skipRowDetector;

        public RowColumnDetectorBuilder(ReadingContext context) {
            this.context = context;
        }

        public RowColumnDetectorBuilder endRowDetector(RowDetector endRowDetector) {
            this.endRowDetector = endRowDetector;
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

        public RowColumnDetectorBuilder skipColumnDetector(ColumnDetector skipColumnDetector) {
            this.skipColumnDetector = skipColumnDetector;
            return this;
        }

        public RowColumnDetector build() {
            return new RowColumnDetector(this);
        }
    }
}
