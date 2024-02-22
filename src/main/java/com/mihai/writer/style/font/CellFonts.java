package com.mihai.writer.style.font;

public class CellFonts {

    private static final CellFont BOLD = CellFont.builder()
            .bold(true)
            .build();

    private static final CellFont ITALIC = CellFont.builder()
            .italic(true)
            .build();

    private CellFonts() {
        throw new IllegalStateException("Utility class");
    }

    public static CellFont bold() {
        return BOLD;
    }

    public static CellFont italic() {
        return ITALIC;
    }
}
