package com.mihai.writer.style.font;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StyleFontsTest {

    @Test
    public void boldFontHasCorrespondingProperty() {
        assertTrue(StyleFonts.bold().isBold());
    }

    @Test
    public void italicFontHasCorrespondingProperty() {
        assertTrue(StyleFonts.italic().isItalic());
    }
}
