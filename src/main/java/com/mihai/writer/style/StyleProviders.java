package com.mihai.writer.style;

import com.mihai.writer.style.color.StyleColor;

public class StyleProviders {

    private static final StyleProvider NO_STYLE_PROVIDER = (context, target) -> WritableCellStyles.noStyle();

    private StyleProviders() {
        throw new IllegalStateException("Utility class");
    }

    public static StyleProvider noStyle() {
        return NO_STYLE_PROVIDER;
    }

    public static StyleProvider of(WritableCellStyle style) {
        return (context, target) -> style;
    }

    public static StyleProvider stripedRows(StyleColor colorA, StyleColor colorB) {
        return (context, target) -> {
            if (context.getCurrentTableRow() % 2 == 0) {
                return WritableCellStyles.backgroundColor(colorA);
            }
            return WritableCellStyles.backgroundColor(colorB);
        };
    }
}
