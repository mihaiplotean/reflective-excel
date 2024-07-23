package com.mihai.assertion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mihai.reader.CellValueFormatter;
import com.mihai.writer.style.WritableCellStyle;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.junit.jupiter.api.Assertions;

public class SheetAssert {

    private static final String CELL_VALUE_DIFFERENCE_MESSAGE = "Sheet \"%s\": Different value for cell %s - expected \"%s\", but got \"%s\"";
    private static final String CELL_STYLE_DIFFERENCE_MESSAGE = "Sheet \"%s\": Expected style differs from actual for properties of cell %s - \"%s\"";

    private final Sheet sheetA;
    private final Sheet sheetB;
    private final Map<Sheet, WritableCellStyleExtractor> cellStyleExtractorProvider;
    private final ExcelAssertSettings settings;

    public SheetAssert(Sheet sheetA, Sheet sheetB, ExcelAssertSettings settings) {
        this.sheetA = sheetA;
        this.sheetB = sheetB;
        this.settings = settings;
        this.cellStyleExtractorProvider = Map.of(
                sheetA, new WritableCellStyleExtractor(sheetA.getWorkbook()),
                sheetB, new WritableCellStyleExtractor(sheetB.getWorkbook())
        );
    }

    public void assertEqualSheets() {
        List<String> differenceMessages = new ArrayList<>();
        for (int row = 0; row < Math.max(sheetA.getLastRowNum(), sheetB.getLastRowNum()); row++) {
            Row rowA = sheetA.getRow(row);
            Row rowB = sheetB.getRow(row);
            for (int column = 0; column < getMaxColumnIndex(rowA, rowB); column++) {
                Cell cellA = rowA == null ? null : rowA.getCell(column);
                Cell cellB = rowB == null ? null : rowB.getCell(column);

                if (isNonFirstCellOfAMergedRegion(cellA)) {
                    // these cells do not have a value and do not impact the style of the merged region
                    continue;
                }
                Object actualValue = getCellValue(cellA);
                Object expectedValue = getCellValue(cellB);
                String currentCellReference = new CellReference(row, column).formatAsString();

                if (areValuesDifferent(actualValue, expectedValue)) {
                    differenceMessages.add(String.format(CELL_VALUE_DIFFERENCE_MESSAGE, sheetA.getSheetName(), currentCellReference,
                                                         expectedValue, actualValue));
                } else {
                    List<String> styleDifference = getStyleDifference(cellA, cellB);
                    if (!styleDifference.isEmpty()) {
                        differenceMessages.add(String.format(CELL_STYLE_DIFFERENCE_MESSAGE, sheetA.getSheetName(), currentCellReference,
                                                             String.join(", ", styleDifference)));
                    }
                }
                if (differenceMessages.size() >= settings.getDifferencesStopCount()) {
                    assertFail(differenceMessages);
                }
            }
        }
        if (!differenceMessages.isEmpty()) {
            assertFail(differenceMessages);
        }
    }

    private int getMaxColumnIndex(Row rowA, Row rowB) {
        if (rowA == null && rowB == null) {
            return 0;
        }
        if (rowA == null) {
            return rowB.getLastCellNum();
        }
        if (rowB == null) {
            return rowA.getLastCellNum();
        }
        return Math.max(rowA.getLastCellNum(), rowB.getLastCellNum());
    }

    private boolean isNonFirstCellOfAMergedRegion(Cell cell) {
        if (cell == null) {
            return false;
        }
        for (CellRangeAddress mergedRegion : cell.getSheet().getMergedRegions()) {
            int cellRow = cell.getRowIndex();
            int cellColumn = cell.getColumnIndex();
            if (mergedRegion.isInRange(cell)
                    && (mergedRegion.getFirstRow() != cellRow || mergedRegion.getFirstColumn() != cellColumn)) {
                return true;
            }
        }
        return false;
    }

    private void assertFail(List<String> differenceMessages) {
        String message = "Sheets named \"%s\" are not equal. Showing first %s differences.\n"
                .formatted(sheetA.getSheetName(), settings.getDifferencesStopCount());
        message += String.join("\n", differenceMessages);
        Assertions.fail(message);
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case NUMERIC -> {
                if(DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue();
                }
                yield cell.getNumericCellValue();
            }
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case BLANK -> "";
            default -> throw new IllegalStateException("Implement value conversion for cell type: " + cell.getCellType());
        };
    }

    private boolean areValuesDifferent(Object valueA, Object valueB) {
        return !areValuesEqual(valueA, valueB);
    }

    private boolean areValuesEqual(Object valueA, Object valueB) {
        if(valueA instanceof String textValueA && valueB instanceof String textValueB) {
            return areValuesEqual(textValueA, textValueB);
        }
        return Objects.equals(valueA, valueB);
    }

    private boolean areValuesEqual(String valueA, String valueB) {
        if (settings.isCaseSensitive()) {
            return StringUtils.equals(valueA, valueB);
        }
        return StringUtils.equalsIgnoreCase(valueA, valueB);
    }

    private List<String> getStyleDifference(Cell actualCell, Cell expectedCell) {
        return new CellStyleComparison(getCellStyle(expectedCell), getCellStyle(actualCell)).getDifferences();
    }

    private WritableCellStyle getCellStyle(Cell cell) {
        if (cell == null) {
            return null;
        }
        return cellStyleExtractorProvider.get(cell.getSheet()).get(cell.getCellStyle());
    }
}
