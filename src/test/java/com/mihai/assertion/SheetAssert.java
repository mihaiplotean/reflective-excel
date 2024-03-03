package com.mihai.assertion;

import com.mihai.reader.CellValueFormatter;
import com.mihai.writer.style.DateFormatUtils;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.style.border.CellBorder;
import com.mihai.writer.style.color.CellColor;
import com.mihai.writer.style.font.CellFont;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class SheetAssert {

    private static final String CELL_VALUE_DIFFERENCE_MESSAGE = "Sheet \"%s\": Different value for cell %s - expected \"%s\", but got \"%s\"";
    private static final String CELL_STYLE_DIFFERENCE_MESSAGE = "Sheet \"%s\": Different style properties for cell %s - \"%s\"";

    private final Sheet sheetA;
    private final Sheet sheetB;
    private final Map<Sheet, CellValueFormatter> cellValueFormatterProvider;
    private final ExcelAssertSettings settings;

    public SheetAssert(Sheet sheetA, Sheet sheetB, ExcelAssertSettings settings) {
        this.sheetA = sheetA;
        this.sheetB = sheetB;
        this.settings = settings;
        this.cellValueFormatterProvider = Map.of(
                sheetA, new CellValueFormatter(sheetA.getWorkbook()),
                sheetB, new CellValueFormatter(sheetB.getWorkbook())
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
        return new CellStyleDifferences(getCellStyle2(sheetA.getWorkbook(), cellA), getCellStyle2(sheetB.getWorkbook(), cellB)).getDifferences();
    }

    private WritableCellStyle getCellStyle2(Workbook workbook, Cell cell) {
        if (cell == null) {
            return null;
        }
        CellStyle style = cell.getCellStyle();
        Font font = workbook.getFontAt(style.getFontIndex());
        return WritableCellStyle.builder()
                .format(getDateFormat(style))
                .horizontalAlignment(style.getAlignment())
                .verticalAlignment(style.getVerticalAlignment())
                .border(
                        CellBorder.builder()
                                .topBorderStyle(style.getBorderTop())
                                .rightBorderStyle(style.getBorderRight())
                                .bottomBorderStyle(style.getBorderBottom())
                                .leftBorderStyle(style.getBorderLeft())
                                .color(getBorderColor(workbook, style))
                                .build()
                )
                .backgroundColor(asColor(style.getFillBackgroundColorColor()))
                .font(
                        CellFont.builder()
                                .name(font.getFontName())
                                .size(font.getFontHeightInPoints())
                                .color(getFontColor(workbook, font))
                                .bold(font.getBold())
                                .italic(font.getItalic())
                                .underLine(font.getUnderline() == Font.U_SINGLE)
                                .build()
                )
                .wrapText(style.getWrapText())
                .build();

    }

    private static String getDateFormat(CellStyle style) {
        if(style.getDataFormat() == DateFormatUtils.DEFAULT_DATE_FORMAT_INDEX) {
            return DateFormatUtils.getLocalizedDatePattern(style.getDataFormatString());
        }
        return style.getDataFormatString();
    }

    private static CellColor getBorderColor(Workbook workbook, CellStyle style) {
        boolean colorsEqual = Stream.of(
                        style.getTopBorderColor(),
                        style.getRightBorderColor(),
                        style.getBottomBorderColor(),
                        style.getLeftBorderColor())
                .distinct()
                .limit(2)
                .count() <= 1;
        if (!colorsEqual) {
            return null;
        }
        if (workbook instanceof HSSFWorkbook xlsWorkbook) {
            HSSFColor color = xlsWorkbook.getCustomPalette().getColor(style.getTopBorderColor());
            return asColor(color);
        }
        if (style instanceof XSSFCellStyle xlsxCellStyle) {
            XSSFColor color = xlsxCellStyle.getTopBorderXSSFColor();
            return asColor(color);
        }
        return null;
    }

    private static CellColor getFontColor(Workbook workbook, Font font) {
        if (workbook instanceof HSSFWorkbook xlsWorkbook) {
            HSSFColor color = xlsWorkbook.getCustomPalette().getColor(font.getColor());
            return asColor(color);
        }
        if (font instanceof XSSFFont xlsxFont) {
            return asColor(xlsxFont.getXSSFColor());
        }
        return null;
    }

    private static CellColor asColor(Color color) {
        if (color instanceof HSSFColor xlsColor) {
            short[] rgbValues = xlsColor.getTriplet();
            return new CellColor((byte) rgbValues[0], (byte) rgbValues[1], (byte) rgbValues[2]);
        }
        if (color instanceof XSSFColor xlsxColor) {
            byte[] rgbValues = xlsxColor.getRGB();
            return new CellColor(rgbValues[0], rgbValues[1], rgbValues[2]);
        }
        return CellColor.BLACK;
    }
}
