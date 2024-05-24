package com.mihai.writer.style.font;

public class CellFonts {

    private static final StyleFont BOLD = StyleFont.builder()
            .bold(true)
            .build();

    private static final StyleFont ITALIC = StyleFont.builder()
            .italic(true)
            .build();

    private CellFonts() {
        throw new IllegalStateException("Utility class");
    }

    public static StyleFont bold() {
        return BOLD;
    }

    public static StyleFont italic() {
        return ITALIC;
    }
}
