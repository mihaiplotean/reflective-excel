package com.mihai.writer.style.border;

import com.mihai.writer.style.font.CellFont;
import org.apache.poi.ss.usermodel.BorderStyle;

public class CellBorders {

    private static final CellBorder ALL_SIDES_THIN = new CellBorder.CellBorderBuilder()
            .topBorderStyle(BorderStyle.THIN)
            .rightBorderStyle(BorderStyle.THIN)
            .bottomBorderStyle(BorderStyle.THIN)
            .leftBorderStyle(BorderStyle.THIN)
            .build();

    private CellBorders() {
        throw new IllegalStateException("Utility class");
    }

    public static CellBorder allSidesThin() {
        return ALL_SIDES_THIN;
    }
}
