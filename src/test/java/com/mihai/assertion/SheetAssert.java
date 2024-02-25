package com.mihai.assertion;

import com.mihai.reader.CellValueFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SheetAssert {

    private static final String CELL_VALUE_DIFFERENCE_MESSAGE = "Sheet %s: Different value for cell %s - \"%s\" vs \"%s\"";
    private static final String CELL_STYLE_DIFFERENCE_MESSAGE = "Sheet %s: Different style properties for cell %s - \"%s\"";

    private final Sheet sheetA;
    private final Sheet sheetB;
    private final Map<Sheet, CellValueFormatter> cellValueFormatterProvider;
    private final ExcelAssertSettings settings;
//    private final List<String> differenceMessages;

    public SheetAssert(Sheet sheetA, Sheet sheetB, ExcelAssertSettings settings) {
        this.sheetA = sheetA;
        this.sheetB = sheetB;
        this.settings = settings;
        this.cellValueFormatterProvider = Map.of(
                sheetA, new CellValueFormatter(sheetA.getWorkbook()),
                sheetB, new CellValueFormatter(sheetB.getWorkbook())
        );
//        this.differenceMessages = new ArrayList<>();
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
                            valueA, valueB));

                    List<String> styleDifference = getStyleDifference(cellA, cellB);
                    if (!styleDifference.isEmpty()) {
                        differenceMessages.add(String.format(CELL_STYLE_DIFFERENCE_MESSAGE, sheetA.getSheetName(), cellA.getAddress(),
                                String.join(", ", styleDifference)));
                    }
                }
                if (differenceMessages.size() >= settings.getDifferencesStopCount()) {
                    String message = "Sheets named %s are not equal. Showing first %s differences\n"
                            .formatted(sheetA.getSheetName(), settings.getDifferencesStopCount());
                    message += String.join("\n", differenceMessages);
                    Assertions.fail(message);
                }
            }
        }
    }

//    private void assertCellValuesEqual(Cell cellA, Cell cellB) {
//        String valueA = getCellValue(cellA);
//        String valueB = getCellValue(cellB);
//
//        if (areValuesDifferent(valueA, valueB)) {
//            Assertions.fail(String.format(CELL_VALUE_DIFFERENCE_MESSAGE, sheetA.getSheetName(), cellA.getAddress(),
//                    valueA, valueB));
//        }
//    }
//
//    private void assertCellStylesEqual(Cell cellA, Cell cellB) {
//        CellStyle styleA = getCellStyle(cellA);
//        CellStyle styleB = getCellStyle(cellB);
//
//        if (styleA == null && styleB == null) {
//            return;
//        }
//
//        List<String> differentProperties = new ArrayList<>();
//
//    }

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
        if (settings.isCaseInsensitive()) {
            return StringUtils.equals(valueA, valueB);
        }
        return StringUtils.equalsIgnoreCase(valueA, valueB);
    }

    private CellStyle getCellStyle(Cell cell) {
        if (cell == null) {
            return null;
        }
        return cell.getCellStyle();
    }

    private List<String> getStyleDifference(Cell cellA, Cell cellB) {
        CellStyle styleA = getCellStyle(cellA);
        CellStyle styleB = getCellStyle(cellB);
        return new CellStyleDifferences(styleA, getFont(sheetA, styleA), styleB, getFont(sheetB, styleB)).getDifferences();
    }

    private Font getFont(Sheet sheet, CellStyle cellStyle) {
        if (cellStyle == null) {
            return null;
        }
        return sheet.getWorkbook().getFontAt(cellStyle.getFontIndex());
    }
}
