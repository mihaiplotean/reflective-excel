package com.mihai.assertion;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.util.*;

import static org.apache.poi.ss.util.CellUtil.*;

public class CellStyleDifferences {

    private static final String FONT_NAME_PROPERTY = "font name";
    private static final String FONT_SIZE_PROPERTY = "font size";
    private static final String FONT_COLOR_PROPERTY = "font color";
    private static final String FONT_BOLD_PROPERTY = "font bold";
    private static final String FONT_ITALIC_PROPERTY = "font italic";
    private static final String FONT_UNDERLINE_PROPERTY = "font underline";

    private final CellStyle styleA;
    private final Font fontA;
    private final CellStyle styleB;
    private final Font fontB;

    public CellStyleDifferences(CellStyle styleA, Font fontA, CellStyle styleB, Font fontB) {
        this.styleA = styleA;
        this.fontA = fontA;
        this.styleB = styleB;
        this.fontB = fontB;
    }

    public List<String> getDifferences() {
        List<String> differences = new ArrayList<>();

        Map<String, Object> propertiesA = getProperties(styleA, fontA);
        Map<String, Object> propertiesB = getProperties(styleB, fontB);

        for (Map.Entry<String, Object> propertyValue : propertiesA.entrySet()) {
            String property = propertyValue.getKey();
            Object valueA = propertyValue.getValue();
            Object valueB = propertiesB.get(property);
            if(!Objects.equals(valueA, valueB)) {
                differences.add(property);
            }
        }
        return differences;
    }

//    private boolean isDataFormatEqual() {
//        return getCellDataFormat(styleA).equals(getCellDataFormat(styleB));
//    }
//
//    private String getCellDataFormat(CellStyle style) {
//        if (style == null) {
//            return "";
//        }
//        return style.getDataFormatString();
//    }
//
//    private boolean isHorizontalAlignmentEqual() {
//        return getHorizontalAlignment(styleA) == getHorizontalAlignment(styleB);
//    }
//
//    private HorizontalAlignment getHorizontalAlignment(CellStyle style) {
//        if (style == null) {
//            return HorizontalAlignment.GENERAL;
//        }
//        return style.getAlignment();
//    }
//
//    private boolean isVerticalAlignmentEqual() {
//        return getVerticalAlignment(styleA) == getVerticalAlignment(styleB);
//    }
//
//    private VerticalAlignment getVerticalAlignment(CellStyle style) {
//        if (style == null) {
//            return VerticalAlignment.BOTTOM;
//        }
//        return style.getVerticalAlignment();
//    }
//
//    private boolean isBackgroundEqual() {
//        if (!Objects.equals(getBackgroundColor(styleA), getBackgroundColor(styleB))) {
//            return false;
//        }
//        return getBackgroundFill(styleA) == getBackgroundFill(styleB);
//    }
//
//    private Color getBackgroundColor(CellStyle style) {
//        if (style == null) {
//            return null;
//        }
//        return style.getFillBackgroundColorColor();
//    }
//
//    private FillPatternType getBackgroundFill(CellStyle style) {
//        if (style == null) {
//            return FillPatternType.NO_FILL;
//        }
//        return style.getFillPattern();
//    }
//
//    private boolean isWrapTextEqual() {
//        return isTextWrap(styleA) == isTextWrap(styleB);
//    }
//
//    private boolean isTextWrap(CellStyle style) {
//        if (style == null) {
//            return false;
//        }
//        return style.getWrapText();
//    }
//
//    private boolean isBorderStyleEqual() {
//        return getTopBorderStyle(styleA) == getTopBorderStyle(styleB)
//                && getRightBorderStyle(styleA) == getRightBorderStyle(styleB)
//                && getBottomBorderStyle(styleA) == getBottomBorderStyle(styleB)
//                && getLeftBorderStyle(styleA) == getBottomBorderStyle(styleB);
//    }
//
//    private boolean isBorderColorEqual() {
//        return getTopBorderColor(styleA) == getTopBorderColor(styleB)
//                && getRightBorderColor(styleA) == getRightBorderColor(styleB)
//                && getBottomBorderColor(styleA) == getBottomBorderColor(styleB)
//                && getLeftBorderColor(styleA) == getBottomBorderColor(styleB);
//    }
//
//    private BorderStyle getTopBorderStyle(CellStyle style) {
//        if (style == null) {
//            return BorderStyle.NONE;
//        }
//        return style.getBorderTop();
//    }
//
//    private BorderStyle getRightBorderStyle(CellStyle style) {
//        if (style == null) {
//            return BorderStyle.NONE;
//        }
//        return style.getBorderRight();
//    }
//
//    private BorderStyle getBottomBorderStyle(CellStyle style) {
//        if (style == null) {
//            return BorderStyle.NONE;
//        }
//        return style.getBorderBottom();
//    }
//
//    private BorderStyle getLeftBorderStyle(CellStyle style) {
//        if (style == null) {
//            return BorderStyle.NONE;
//        }
//        return style.getBorderLeft();
//    }
//
//    private short getTopBorderColor(CellStyle style) {
//        if (style == null) {
//            return -1;
//        }
//        return style.getTopBorderColor();
//    }
//
//    private short getRightBorderColor(CellStyle style) {
//        if (style == null) {
//            return -1;
//        }
//        return style.getRightBorderColor();
//    }
//
//    private short getBottomBorderColor(CellStyle style) {
//        if (style == null) {
//            return -1;
//        }
//        return style.getBottomBorderColor();
//    }
//
//    private short getLeftBorderColor(CellStyle style) {
//        if (style == null) {
//            return -1;
//        }
//        return style.getLeftBorderColor();
//    }

    private static Map<String, Object> getProperties(CellStyle style, Font font) {
        Map<String, Object> properties = new HashMap<>();

        if(style == null) {
            putDefaultStyleProperties(properties);
            return properties;
        }

        properties.put(ALIGNMENT, style.getAlignment());
        properties.put(VERTICAL_ALIGNMENT, style.getVerticalAlignment());
        properties.put(BORDER_BOTTOM, style.getBorderBottom());
        properties.put(BORDER_LEFT, style.getBorderLeft());
        properties.put(BORDER_RIGHT, style.getBorderRight());
        properties.put(BORDER_TOP, style.getBorderTop());
        properties.put(BOTTOM_BORDER_COLOR, style.getBottomBorderColor());
        properties.put(DATA_FORMAT, style.getDataFormat());
        properties.put(FILL_PATTERN, style.getFillPattern());

        properties.put(FILL_FOREGROUND_COLOR, style.getFillForegroundColor());
        properties.put(FILL_BACKGROUND_COLOR, style.getFillBackgroundColor());
        properties.put(FILL_FOREGROUND_COLOR_COLOR, style.getFillForegroundColorColor());
        properties.put(FILL_BACKGROUND_COLOR_COLOR, style.getFillBackgroundColorColor());
        properties.put(HIDDEN, style.getHidden());
        properties.put(INDENTION, style.getIndention());
        properties.put(LEFT_BORDER_COLOR, style.getLeftBorderColor());
        properties.put(LOCKED, style.getLocked());
        properties.put(RIGHT_BORDER_COLOR, style.getRightBorderColor());
        properties.put(TOP_BORDER_COLOR, style.getTopBorderColor());
        properties.put(WRAP_TEXT, style.getWrapText());
        properties.put(SHRINK_TO_FIT, style.getShrinkToFit());
        properties.put(QUOTE_PREFIXED, style.getQuotePrefixed());

        properties.put(FONT_NAME_PROPERTY, font.getFontName());
        properties.put(FONT_SIZE_PROPERTY, font.getFontHeightInPoints());
        properties.put(FONT_COLOR_PROPERTY, font.getColor());
        properties.put(FONT_BOLD_PROPERTY, font.getBold());
        properties.put(FONT_ITALIC_PROPERTY, font.getItalic());
        properties.put(FONT_UNDERLINE_PROPERTY, font.getUnderline());

        return properties;
    }

    private static void putDefaultStyleProperties(Map<String, Object> properties) {
        properties.put(ALIGNMENT, HorizontalAlignment.CENTER);
        properties.put(VERTICAL_ALIGNMENT, VerticalAlignment.BOTTOM);
        properties.put(BORDER_BOTTOM, BorderStyle.NONE);
        properties.put(BORDER_LEFT, BorderStyle.NONE);
        properties.put(BORDER_RIGHT, BorderStyle.NONE);
        properties.put(BORDER_TOP, BorderStyle.NONE);
        properties.put(BOTTOM_BORDER_COLOR, 0);
        properties.put(DATA_FORMAT, "");
        properties.put(FILL_PATTERN, FillPatternType.NO_FILL);

        properties.put(FILL_FOREGROUND_COLOR, 0);
        properties.put(FILL_BACKGROUND_COLOR, 0);
        properties.put(FILL_FOREGROUND_COLOR_COLOR, null);
        properties.put(FILL_BACKGROUND_COLOR_COLOR, null);
        properties.put(HIDDEN, false);
        properties.put(INDENTION, 0);
        properties.put(LEFT_BORDER_COLOR, 0);
        properties.put(LOCKED, false);
        properties.put(RIGHT_BORDER_COLOR, 0);
        properties.put(TOP_BORDER_COLOR, 0);
        properties.put(WRAP_TEXT, false);
        properties.put(SHRINK_TO_FIT, false);
        properties.put(QUOTE_PREFIXED, false);

        properties.put(FONT_BOLD_PROPERTY, false);
        properties.put(FONT_ITALIC_PROPERTY, false);
        properties.put(FONT_UNDERLINE_PROPERTY, false);

        // assumes we use .xlsx files!
        properties.put(FONT_NAME_PROPERTY, XSSFFont.DEFAULT_FONT_NAME);
        properties.put(FONT_SIZE_PROPERTY, XSSFFont.DEFAULT_FONT_SIZE);
        properties.put(FONT_COLOR_PROPERTY, XSSFFont.DEFAULT_FONT_COLOR);
    }
}
