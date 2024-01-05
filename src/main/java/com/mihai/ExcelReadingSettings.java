package com.mihai;

import com.mihai.detector.RowDetector;
import org.apache.poi.ss.util.CellReference;

public class ExcelReadingSettings {

    public static final ExcelReadingSettings DEFAULT = new ExcelReadingSettingsBuilder().create();

    private final String sheetName;
    private final int sheetIndex;
    private final CellReference headerStartCellReference;
    private final RowDetector endRowDetector;

    private ExcelReadingSettings(ExcelReadingSettingsBuilder builder) {
        sheetName = builder.sheetName;
        sheetIndex = builder.sheetIndex;
        headerStartCellReference = new CellReference(builder.headerStartCellReference);
        endRowDetector = builder.endRowDetector;
    }

    public String getSheetName() {
        return sheetName;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public int getHeaderStartRow() {
        return headerStartCellReference.getRow();
    }

    public int getHeaderStartColumn() {
        return headerStartCellReference.getCol();
    }

    public boolean isEndRow(RowCells rowCells) {
        return endRowDetector.test(rowCells);
    }

    public static ExcelReadingSettingsBuilder with() {
        return new ExcelReadingSettingsBuilder();
    }

    public static final class ExcelReadingSettingsBuilder {

        private static final RowDetector ALL_CELLS_EMPTY = RowCells::allEmpty;

        private String sheetName;
        private int sheetIndex;
        private String headerStartCellReference = "A1";
//        private RowDetector headerRowDetector =
//        private RowDetector startRowDetector =
//        private RowDetector skipRowDetector =
        private RowDetector endRowDetector = ALL_CELLS_EMPTY;

        public ExcelReadingSettingsBuilder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public ExcelReadingSettingsBuilder sheetIndex(int sheetIndex) {
            this.sheetIndex = sheetIndex;
            return this;
        }

        public ExcelReadingSettingsBuilder headerStartCellReference(String headerStartCellReference) {
            this.headerStartCellReference = headerStartCellReference;
            return this;
        }

        public ExcelReadingSettingsBuilder endRowDetector(RowDetector endRowDetector) {
            this.endRowDetector = endRowDetector;
            return this;
        }

        public ExcelReadingSettings create() {
            return new ExcelReadingSettings(this);
        }
    }
}
