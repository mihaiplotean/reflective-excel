package com.mihai.writer.style.font;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StyleFontsTest {

    @Test
    public void boldFontHasCorrespondingProperty() {
        assertTrue(StyleFonts.bold().isBold());
    }

    @Test
    public void italicFontHasCorrespondingProperty() {
        assertTrue(StyleFonts.italic().isItalic());
    }
}