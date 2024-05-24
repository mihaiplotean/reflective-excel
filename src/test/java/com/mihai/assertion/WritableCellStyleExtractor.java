package com.mihai.assertion;

import com.mihai.common.utils.DateFormatUtils;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.style.border.CellBorder;
import com.mihai.writer.style.color.StyleColor;
import com.mihai.writer.style.font.StyleFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.util.stream.Stream;

public class WritableCellStyleExtractor {

    private final Workbook workbook;

    public WritableCellStyleExtractor(Workbook workbook) {
        this.workbook = workbook;
    }

    public WritableCellStyle get(CellStyle style) {
        return WritableCellStyle.builder()
                .format(getDataFormat(style))
                .horizontalAlignment(style.getAlignment())
                .verticalAlignment(style.getVerticalAlignment())
                .border(getBorder(style))
                .backgroundColor(asColor(style.getFillBackgroundColorColor()))
                .font(getFont(workbook.getFontAt(style.getFontIndex())))
                .wrapText(style.getWrapText())
                .build();

    }

    private static String getDataFormat(CellStyle style) {
        if(style.getDataFormat() == DateFormatUtils.DEFAULT_DATE_FORMAT_INDEX) {
            return DateFormatUtils.getLocalizedDatePattern(style.getDataFormatString());
        }
        return style.getDataFormatString();
    }

    private CellBorder getBorder(CellStyle style) {
        return CellBorder.builder()
                .topBorderStyle(style.getBorderTop())
                .rightBorderStyle(style.getBorderRight())
                .bottomBorderStyle(style.getBorderBottom())
                .leftBorderStyle(style.getBorderLeft())
                .color(getBorderColor(workbook, style))
                .build();
    }

    private StyleFont getFont(Font font) {
        return StyleFont.builder()
                .name(font.getFontName())
                .size(font.getFontHeightInPoints())
                .color(getFontColor(workbook, font))
                .bold(font.getBold())
                .italic(font.getItalic())
                .underLine(font.getUnderline() == Font.U_SINGLE)
                .build();
    }

    private static StyleColor getBorderColor(Workbook workbook, CellStyle style) {
        boolean colorsEqual = Stream.of(
                        style.getTopBorderColor(),
                        style.getRightBorderColor(),
                        style.getBottomBorderColor(),
                        style.getLeftBorderColor())
                .distinct()
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

    private static StyleColor getFontColor(Workbook workbook, Font font) {
        if (workbook instanceof HSSFWorkbook xlsWorkbook) {
            HSSFColor color = xlsWorkbook.getCustomPalette().getColor(font.getColor());
            return asColor(color);
        }
        if (font instanceof XSSFFont xlsxFont) {
            return asColor(xlsxFont.getXSSFColor());
        }
        return null;
    }

    private static StyleColor asColor(Color color) {
        if (color instanceof HSSFColor xlsColor) {
            short[] rgbValues = xlsColor.getTriplet();
            return new StyleColor(rgbValues[0], rgbValues[1], rgbValues[2]);
        }
        if (color instanceof XSSFColor xlsxColor) {
            byte[] rgbValues = xlsxColor.getRGB();
            return new StyleColor(rgbValues[0], rgbValues[1], rgbValues[2]);
        }
        return StyleColor.BLACK;
    }
}
