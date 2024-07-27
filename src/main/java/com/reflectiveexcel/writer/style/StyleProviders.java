package com.reflectiveexcel.writer.style;

import com.reflectiveexcel.writer.style.color.StyleColor;

public class StyleProviders {

    private static final StyleProvider NO_STYLE_PROVIDER = (context, target) -> WritableCellStyles.noStyle();

    private StyleProviders() {
        throw new IllegalStateException("Utility class");
    }

    public static StyleProvider noStyle() {
        return NO_STYLE_PROVIDER;
    }

    /**
     * Style provider which returns the input style.
     * This means that the style does not depend on the state of the writing context.
     *
     * @param style style to be returned.
     * @return input style.
     */
    public static StyleProvider of(WritableCellStyle style) {
        return (context, target) -> style;
    }

    /**
     * Intended to be used for altering the row color in a table.
     *
     * @param colorA the color of the odd table rows.
     * @param colorB the color of the even table rows.
     * @return style provider for the row cells.
     */
    public static StyleProvider stripedRows(StyleColor colorA, StyleColor colorB) {
        return (context, target) -> {
            if (context.getCurrentTableRow() % 2 == 0) {
                return WritableCellStyles.backgroundColor(colorA);
            }
            return WritableCellStyles.backgroundColor(colorB);
        };
    }
}
