package com.mihai.assertion;

import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.style.border.CellBorder;
import com.mihai.writer.style.font.StyleFont;

import java.util.*;

import static org.apache.poi.ss.util.CellUtil.*;

public class CellStyleComparison {

    private static final String BACKGROUND_COLOR = "background color";
    private static final String BORDER_COLOR = "border color";
    private static final String FONT_NAME_PROPERTY = "font name";
    private static final String FONT_SIZE_PROPERTY = "font size";
    private static final String FONT_COLOR_PROPERTY = "font color";
    private static final String FONT_BOLD_PROPERTY = "font bold";
    private static final String FONT_ITALIC_PROPERTY = "font italic";
    private static final String FONT_UNDERLINE_PROPERTY = "font underline";

    private final WritableCellStyle cellStyleA;
    private final WritableCellStyle cellStyleB;

    public CellStyleComparison(WritableCellStyle cellStyleA, WritableCellStyle cellStyleB) {
        this.cellStyleA = cellStyleA;
        this.cellStyleB = cellStyleB;
    }

    public List<String> getDifferences() {
        List<String> differences = new ArrayList<>();

        Map<String, Object> propertiesA = getProperties(cellStyleA);
        Map<String, Object> propertiesB = getProperties(cellStyleB);

        for (String property : union(propertiesA.keySet(), propertiesB.keySet())) {
            Object valueA = propertiesA.get(property);
            Object valueB = propertiesB.get(property);
            if (!Objects.equals(valueA, valueB)) {
                differences.add(property + ": " + "<" + valueA + ">" + " vs " + "<" + valueB + ">");
            }
        }
        return differences;
    }

    private static Map<String, Object> getProperties(WritableCellStyle style) {
        if(style == null) {
            return Map.of();
        }
        Map<String, Object> properties = new HashMap<>();

        properties.put(DATA_FORMAT, style.getFormat());
        properties.put(ALIGNMENT, style.getHorizontalAlignment());
        properties.put(VERTICAL_ALIGNMENT, style.getVerticalAlignment());
        properties.put(BACKGROUND_COLOR, style.getBackgroundColor());
        properties.put(WRAP_TEXT, style.isWrapText());

        properties.putAll(getBorderProperties(style.getBorder()));
        properties.putAll(getFontProperties(style.getFont()));

        return properties;
    }

    private static Map<String, Object> getBorderProperties(CellBorder border) {
        Map<String, Object> properties = new HashMap<>();

        properties.put(BORDER_BOTTOM, border.getBottomBorderStyle());
        properties.put(BORDER_LEFT, border.getLeftBorderStyle());
        properties.put(BORDER_RIGHT, border.getRightBorderStyle());
        properties.put(BORDER_TOP, border.getTopBorderStyle());

        properties.put(BORDER_COLOR, border.getColor());

        return properties;
    }

    private static Map<String, Object> getFontProperties(StyleFont font) {
        Map<String, Object> properties = new HashMap<>();

        properties.put(FONT_NAME_PROPERTY, font.getName());
        properties.put(FONT_SIZE_PROPERTY, font.getSize());
        properties.put(FONT_COLOR_PROPERTY, font.getColor());

        properties.put(FONT_BOLD_PROPERTY, font.isBold());
        properties.put(FONT_ITALIC_PROPERTY, font.isItalic());
        properties.put(FONT_UNDERLINE_PROPERTY, font.isUnderLine());

        return properties;
    }

    private static Set<String> union(Collection<String> collectionA, Collection<String> collectionB) {
        Set<String> unionSet = new HashSet<>(collectionA);
        unionSet.addAll(collectionB);
        return unionSet;
    }
}
