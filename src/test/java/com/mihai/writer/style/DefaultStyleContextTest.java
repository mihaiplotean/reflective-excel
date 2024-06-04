package com.mihai.writer.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mihai.writer.style.color.StyleColor;
import org.junit.jupiter.api.Test;

public class DefaultStyleContextTest {

    @Test
    public void defaultHeaderStyleIsNone() {
        DefaultStyleContext styleContext = new DefaultStyleContext();
        assertEquals(WritableCellStyles.noStyle(), styleContext.getHeaderStyle(null, "unused"));
    }

    @Test
    public void defaultTypeStyleIsNone() {
        DefaultStyleContext styleContext = new DefaultStyleContext();
        assertEquals(WritableCellStyles.noStyle(), styleContext.getTypeStyle(null, "unused"));
    }

    @Test
    public void defaultRowStyleIsNone() {
        DefaultStyleContext styleContext = new DefaultStyleContext();
        assertEquals(WritableCellStyles.noStyle(), styleContext.getRowStyle(null, "unused"));
    }

    @Test
    public void defaultColumnStyleIsNone() {
        DefaultStyleContext styleContext = new DefaultStyleContext();
        assertEquals(WritableCellStyles.noStyle(), styleContext.getColumnStyle(null, "unused"));
    }

    @Test
    public void defaultCellStyleIsNone() {
        DefaultStyleContext styleContext = new DefaultStyleContext();
        assertEquals(WritableCellStyles.noStyle(), styleContext.getCellStyle(null, "unused"));
    }

    @Test
    public void headerStyleIsReturned() {
        DefaultStyleContext styleContext = new DefaultStyleContext();
        styleContext.setHeaderStyleProvider(StyleProviders.of(WritableCellStyles.boldText()));

        assertTrue(styleContext.getHeaderStyle(null, "unused").getFont().isBold());
    }

    @Test
    public void typeStyleIsReturned() {
        DefaultStyleContext styleContext = new DefaultStyleContext();
        styleContext.setTypeStyleProvider(String.class, StyleProviders.of(WritableCellStyles.backgroundColor(1, 1, 1)));
        styleContext.setTypeStyleProvider(Integer.class, StyleProviders.of(WritableCellStyles.backgroundColor(2, 2, 2)));

        assertEquals(new StyleColor(1, 1, 1), styleContext.getTypeStyle(null, "test").getBackgroundColor());
        assertEquals(new StyleColor(2, 2, 2), styleContext.getTypeStyle(null, 42).getBackgroundColor());
    }

    @Test
    public void rowStyleIsReturned() {
        DefaultStyleContext styleContext = new DefaultStyleContext();
        styleContext.setRowStyleProvider(StyleProviders.of(WritableCellStyles.boldText()));

        assertTrue(styleContext.getRowStyle(null, "unused").getFont().isBold());
    }

    @Test
    public void columnStyleIsReturned() {
        DefaultStyleContext styleContext = new DefaultStyleContext();
        styleContext.setColumnStyleProvider(StyleProviders.of(WritableCellStyles.boldText()));

        assertTrue(styleContext.getColumnStyle(null, "unused").getFont().isBold());
    }

    @Test
    public void cellStyleIsReturned() {
        DefaultStyleContext styleContext = new DefaultStyleContext();
        styleContext.setCellStyleProvider(StyleProviders.of(WritableCellStyles.boldText()));

        assertTrue(styleContext.getCellStyle(null, "unused").getFont().isBold());
    }
}
