package com.mihai.assertion;

import com.mihai.reader.CellValueFormatter;
import com.mihai.writer.style.WritableCellStyle;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SheetAssert {

    private static final String CELL_VALUE_DIFFERENCE_MESSAGE = "Sheet \"%s\": Different value for cell %s - expected \"%s\", but got \"%s\"";
    private static final String CELL_STYLE_DIFFERENCE_MESSAGE = "Sheet \"%s\": Different style properties for cell %s - \"%s\"";

    private final Sheet sheetA;
    private final Sheet sheetB;
    private final Map<Sheet, CellValueFormatter> cellValueFormatterProvider;
    private final Map<Sheet, WritableCellStyleExtractor> cellStyleExtractorProvider;
    private final ExcelAssertSettings settings;

    public SheetAssert(Sheet sheetA, Sheet sheetB, ExcelAssertSettings settings) {
        this.sheetA = sheetA;
        this.sheetB = sheetB;
        this.settings = settings;
        this.cellValueFormatterProvider = Map.of(
                sheetA, new CellValueFormatter(sheetA.getWorkbook()),
                sheetB, new CellValueFormatter(sheetB.getWorkbook())
        );
        this.cellStyleExtractorProvider = Map.of(
                sheetA, new WritableCellStyleExtractor(sheetA.getWorkbook()),
                sheetB, new WritableCellStyleExtractor(sheetB.getWorkbook())
        );
    }

    public void assertEqualSheets() {
        List<String> differenceMessages = new ArrayList<>();
        for (Row rowA : sheetA) {
            for (Cell cellA : rowA) {
                Cell cellB = getCell(sheetB, cellA.getRowIndex(), cellA.getColumnIndex());

                String valueA = getCellValue(cellA);
                String valueB = getCellValue(cellB);

                if (areValuesDifferent(valueA, valueB)) {
                    differenceMessages.add(String.format(CELL_VALUE_DIFFERENCE_MESSAGE, sheetA.getSheetName(), cellA.getAddress(),
                            valueB, valueA));
                } else {
                    List<String> styleDifference = getStyleDifference(cellA, cellB);
                    if (!styleDifference.isEmpty()) {
                        differenceMessages.add(String.format(CELL_STYLE_DIFFERENCE_MESSAGE, sheetA.getSheetName(), cellA.getAddress(),
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

    private void assertFail(List<String> differenceMessages) {
        String message = "Sheets named \"%s\" are not equal. Showing first %s differences.\n"
                .formatted(sheetA.getSheetName(), settings.getDifferencesStopCount());
        message += String.join("\n", differenceMessages);
        Assertions.fail(message);
    }

    private static Cell getCell(Sheet sheet, int rowIndex, int columnIndex) {
        return Optional.ofNullable(sheet.getRow(rowIndex))
                .map(row -> row.getCell(columnIndex))
                .orElse(null);
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        return cellValueFormatterProvider.get(cell.getSheet()).toString(cell);
    }

    private boolean areValuesDifferent(String valueA, String valueB) {
        return !areValuesEqual(valueA, valueB);
    }

    private boolean areValuesEqual(String valueA, String valueB) {
        if (settings.isCaseSensitive()) {
            return StringUtils.equals(valueA, valueB);
        }
        return StringUtils.equalsIgnoreCase(valueA, valueB);
    }

    private List<String> getStyleDifference(Cell cellA, Cell cellB) {
        return new CellStyleComparison(getCellStyle(cellA), getCellStyle(cellB)).getDifferences();
    }

    private WritableCellStyle getCellStyle(Cell cell) {
        if (cell == null) {
            return null;
        }
        return cellStyleExtractorProvider.get(cell.getSheet()).get(cell.getCellStyle());
    }
}
