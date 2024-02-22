package com.mihai.writer.style;

import com.mihai.writer.style.border.CellBorders;
import com.mihai.writer.style.color.CellColor;
import com.mihai.writer.style.font.CellFonts;

public final class WritableCellStyles {

    private static final WritableCellStyle BOLD_TEXT = WritableCellStyle.builder()
            .font(CellFonts.bold())
            .build();

    private static final WritableCellStyle ALL_SIDE_BORDER = WritableCellStyle.builder()
            .border(CellBorders.allSidesThin())
            .build();

    private WritableCellStyles() {
        throw new IllegalStateException("Utility class");
    }

    public static WritableCellStyle boldText() {
        return BOLD_TEXT;
    }

    public static WritableCellStyle allSideBorder() {
        return ALL_SIDE_BORDER;
    }

    public static WritableCellStyle backgroundColor(int red, int green, int blue) {
        return WritableCellStyle.builder()
                .backgroundColor(new CellColor((byte) red, (byte) green, (byte) blue))
                .build();
    }
}
