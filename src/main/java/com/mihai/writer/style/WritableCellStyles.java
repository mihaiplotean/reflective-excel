package com.mihai.writer.style;

import com.mihai.core.utils.DateFormatUtils;
import com.mihai.writer.style.border.CellBorders;
import com.mihai.writer.style.color.StyleColor;
import com.mihai.writer.style.font.StyleFonts;

public final class WritableCellStyles {

    private static final WritableCellStyle NO_STYLE = WritableCellStyle.builder().build();

    private static final WritableCellStyle BOLD_TEXT = WritableCellStyle.builder()
            .font(StyleFonts.bold())
            .build();

    private static final WritableCellStyle ALL_SIDE_BORDER = WritableCellStyle.builder()
            .border(CellBorders.allSidesThin())
            .build();

    private WritableCellStyles() {
        throw new IllegalStateException("Utility class");
    }

    public static WritableCellStyle noStyle() {
        return NO_STYLE;
    }

    public static WritableCellStyle forDate() {
        return WritableCellStyle.builder()
                .format(DateFormatUtils.getLocalizedDatePattern())
                .build();
    }

    public static WritableCellStyle boldText() {
        return BOLD_TEXT;
    }

    public static WritableCellStyle allSideBorder() {
        return ALL_SIDE_BORDER;
    }

    public static WritableCellStyle backgroundColor(int red, int green, int blue) {
        return backgroundColor(new StyleColor(red, green, blue));
    }

    public static WritableCellStyle backgroundColor(StyleColor color) {
        return WritableCellStyle.builder()
                .backgroundColor(color)
                .build();
    }

    public static WritableCellStyle format(String format) {
        return WritableCellStyle.builder()
                .format(format)
                .build();
    }
}
