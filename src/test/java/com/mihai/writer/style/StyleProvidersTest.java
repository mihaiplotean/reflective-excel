package com.mihai.writer.style;

import com.mihai.common.CellPointer;
import com.mihai.writer.WritingContext;
import com.mihai.writer.style.color.StyleColor;
import com.mihai.writer.table.TableWritingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StyleProvidersTest {

    @Test
    public void noStyleProviderDoesNothing() {
        StyleProvider styleProvider = StyleProviders.noStyle();
        WritableCellStyle style = styleProvider.getStyle(null, null);

        assertEquals(WritableCellStyle.builder().build(), style);
    }

    @Test
    public void identityProviderReturnsPassedStyle() {
        StyleProvider styleProvider = StyleProviders.of(WritableCellStyles.boldText());
        WritableCellStyle style = styleProvider.getStyle(null, null);

        assertEquals(WritableCellStyles.boldText(), style);
    }

    @Test
    public void stripedRowsProviderAlternatesRowColors() {
        StyleProvider styleProvider = StyleProviders.stripedRows(new StyleColor(1, 1, 1), new StyleColor(2, 2, 2));

        TableWritingContext tableWritingContext = new TableWritingContext();
        tableWritingContext.setWritingTable(true);
        WritingContext writingContext = new WritingContext(tableWritingContext, new CellPointer());

        tableWritingContext.setCurrentTableRow(0);
        assertEquals(new StyleColor(1, 1, 1), styleProvider.getStyle(writingContext, "even row").getBackgroundColor());

        tableWritingContext.setCurrentTableRow(1);
        assertEquals(new StyleColor(2, 2, 2), styleProvider.getStyle(writingContext, "odd row").getBackgroundColor());
    }
}
