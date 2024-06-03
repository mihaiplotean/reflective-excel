package com.mihai.writer.style;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.ExtendedColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class ColorStyleUtils {

    public static void setBorderColor(CellStyle style, Color color) {
        if (style instanceof XSSFCellStyle xssfCellStyle) {
            XSSFColor xssfColor = (XSSFColor) color;
            xssfCellStyle.setTopBorderColor(xssfColor);
            xssfCellStyle.setRightBorderColor(xssfColor);
            xssfCellStyle.setBottomBorderColor(xssfColor);
            xssfCellStyle.setLeftBorderColor(xssfColor);
        } else if (style instanceof HSSFCellStyle hssfCellStyle) {
            short colorIndex = ((HSSFColor) color).getIndex();
            hssfCellStyle.setTopBorderColor(colorIndex);
            hssfCellStyle.setRightBorderColor(colorIndex);
            hssfCellStyle.setBottomBorderColor(colorIndex);
            hssfCellStyle.setLeftBorderColor(colorIndex);
        } else {
            assert false : "Unknown cell style";
        }
    }

    public static void setFontColor(Font font, Color color) {
        if (font instanceof XSSFFont xssfFont) {
            xssfFont.setColor((XSSFColor) color);
        } else if (font instanceof HSSFFont hssfFont) {
            hssfFont.setColor(((HSSFColor) color).getIndex());
        } else {
            assert false : "Unknown font type";
        }
    }

    /**
     * For xls files, a similar color is returned from the existing color palette, as the palette is limited in size.
     */
    public static Color createColor(Workbook workbook, byte red, byte green, byte blue) {
        if (workbook instanceof HSSFWorkbook xlsWorkbook) {
            return findExistingSimilarXlsColor(xlsWorkbook, red, green, blue);
        }
        ExtendedColor color = workbook.getCreationHelper().createExtendedColor();
        color.setRGB(new byte[]{red, green, blue});
        return color;
    }

    private static HSSFColor findExistingSimilarXlsColor(HSSFWorkbook xlsWorkbook, byte red, byte green, byte blue) {
        return xlsWorkbook.getCustomPalette().findSimilarColor(red, green, blue);
    }
}
