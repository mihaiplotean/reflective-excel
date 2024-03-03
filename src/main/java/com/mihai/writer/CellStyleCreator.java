package com.mihai.writer;

import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.style.border.CellBorder;
import com.mihai.writer.style.color.CellColor;
import com.mihai.writer.style.font.CellFont;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.util.HashMap;
import java.util.Map;

public class CellStyleCreator {

    private final Workbook workbook;
    private final CreationHelper creationHelper;

    private final Map<WritableCellStyle, CellStyle> styleCache = new HashMap<>();
    private final Map<String, Short> formatToIndexMap = new HashMap<>();
    private final Map<CellColor, Color> colorMap = new HashMap<>();
    private final Map<CellFont, Font> fontMap = new HashMap<>();

    public CellStyleCreator(Workbook workbook) {
        this.workbook = workbook;
        this.creationHelper = workbook.getCreationHelper();
    }

    public CellStyle getCellStyle(WritableCellStyle style) {
        return styleCache.computeIfAbsent(style, this::createCellStyle);
    }

    private CellStyle createCellStyle(WritableCellStyle style) {
        CellStyle cellStyle = workbook.createCellStyle();

        setDataFormat(style, cellStyle);
        setHorizontalAlignment(style, cellStyle);
        setVerticalAlignment(style, cellStyle);
        setBorder(style, cellStyle);
        setFont(style, cellStyle);
        setBackgroundColor(style, cellStyle);
        setTextWrap(style, cellStyle);

        return cellStyle;
    }

    private void setDataFormat(WritableCellStyle style, CellStyle cellStyle) {
        String format = style.getFormat();
        if (!StringUtils.isEmpty(format)) {
            cellStyle.setDataFormat(findOrCreateFormat(format));
        }
    }

    private short findOrCreateFormat(String format) {
        return formatToIndexMap.computeIfAbsent(format, this::createFormat);
    }

    private short createFormat(String format) {
        return creationHelper.createDataFormat().getFormat(format);
    }

    private static void setHorizontalAlignment(WritableCellStyle style, CellStyle cellStyle) {
        HorizontalAlignment horizontalAlignment = style.getHorizontalAlignment();
        if (horizontalAlignment != null) {
            cellStyle.setAlignment(horizontalAlignment);
        }
    }

    private static void setVerticalAlignment(WritableCellStyle style, CellStyle cellStyle) {
        VerticalAlignment verticalAlignment = style.getVerticalAlignment();
        if (verticalAlignment != null) {
            cellStyle.setVerticalAlignment(verticalAlignment);
        }
    }

    private void setBackgroundColor(WritableCellStyle style, CellStyle cellStyle) {
        CellColor backgroundColor = style.getBackgroundColor();
        if (backgroundColor != null) {
            Color color = colorMap.computeIfAbsent(backgroundColor, this::createColor);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setFillForegroundColor(color);
        }
    }

    private Color createColor(CellColor cellColor) {
        if(workbook instanceof HSSFWorkbook xlsWorkbook) {
            return findOrCreateXlsColor(xlsWorkbook, cellColor);
        }
        ExtendedColor color = creationHelper.createExtendedColor();
        color.setRGB(new byte[]{cellColor.getRed(), cellColor.getGreen(), cellColor.getBlue()});
        return color;
    }

    private static HSSFColor findOrCreateXlsColor(HSSFWorkbook xlsWorkbook, CellColor cellColor) {
        HSSFPalette palette = xlsWorkbook.getCustomPalette();
        HSSFColor color = palette.findColor(cellColor.getRed(), cellColor.getGreen(), cellColor.getBlue());
        if(color == null) {
            return palette.addColor(cellColor.getRed(), cellColor.getGreen(), cellColor.getBlue());
        }
        return color;
    }

    private void setFont(WritableCellStyle style, CellStyle cellStyle) {
        CellFont font = style.getFont();
        if (font != null) {
            applyFont(cellStyle, font);
        }
    }

    private void setBorder(WritableCellStyle style, CellStyle cellStyle) {
        CellBorder border = style.getBorder();
        if (border != null) {
            applyBorder(cellStyle, border);
        }
    }

    private void applyBorder(CellStyle cellStyle, CellBorder border) {
        BorderStyle topBorderStyle = border.getTopBorderStyle();
        if (topBorderStyle != null) {
            cellStyle.setBorderTop(topBorderStyle);
        }
        BorderStyle rightBorderStyle = border.getRightBorderStyle();
        if (rightBorderStyle != null) {
            cellStyle.setBorderRight(rightBorderStyle);
        }
        BorderStyle bottomBorderStyle = border.getBottomBorderStyle();
        if (bottomBorderStyle != null) {
            cellStyle.setBorderBottom(bottomBorderStyle);
        }
        BorderStyle leftBorderStyle = border.getLeftBorderStyle();
        if (leftBorderStyle != null) {
            cellStyle.setBorderLeft(leftBorderStyle);
        }
        CellColor borderColor = border.getColor();
        if (borderColor != null) {
            Color color = colorMap.computeIfAbsent(borderColor, this::createColor);
            cellStyle.setTopBorderColor(getColorIndex(color));
        }
    }

    private void applyFont(CellStyle cellStyle, CellFont fontReference) {
        Font font = fontMap.computeIfAbsent(fontReference, this::createFont);
        cellStyle.setFont(font);
    }

    private Font createFont(CellFont fontReference) {
        Font font = workbook.createFont();

        String fontName = fontReference.getName();
        if(!StringUtils.isEmpty(fontName)) {
            font.setFontName(fontName);
        }
        short fontSize = fontReference.getSize();
        if(fontSize > 0) {
            font.setFontHeightInPoints(fontSize);
        }
        CellColor fontColor = fontReference.getColor();
        if(fontColor != null) {
            Color color = colorMap.computeIfAbsent(fontColor, this::createColor);
            font.setColor(getColorIndex(color));
        }
        font.setBold(fontReference.isBold());
        font.setItalic(fontReference.isItalic());
        if (fontReference.isUnderLine()) {
            font.setUnderline(Font.U_SINGLE);
        }
        return font;
    }

    private static short getColorIndex(Color color) {
        if(color instanceof HSSFColor xlsColor) {
            return xlsColor.getIndex();
        }
        if(color instanceof XSSFColor xlsxColor) {
            return xlsxColor.getIndex();
        }
        return 0;
    }

    private static void setTextWrap(WritableCellStyle style, CellStyle cellStyle) {
        cellStyle.setWrapText(style.isWrapText());
    }
}
