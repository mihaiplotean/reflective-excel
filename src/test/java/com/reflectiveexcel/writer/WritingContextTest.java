package com.reflectiveexcel.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.reflectiveexcel.core.CellPointer;
import com.reflectiveexcel.writer.table.TableWritingContext;
import com.reflectiveexcel.writer.table.WrittenTableHeader;
import com.reflectiveexcel.writer.table.WrittenTableHeaders;
import org.junit.jupiter.api.Test;

public class WritingContextTest {

    @Test
    public void currentColumnEmptyWhenNoHeadersSpecified() {
        TableWritingContext tableWritingContext = new TableWritingContext();
        CellPointer cellPointer = new CellPointer();
        WritingContext writingContext = new WritingContext(tableWritingContext, cellPointer);

        String currentColumnName = writingContext.getCurrentColumnName();
        assertEquals("", currentColumnName);
    }

    @Test
    public void currentColumnNameCorrectWhenHeadersSpecifiedAndInWritingState() {
        TableWritingContext tableWritingContext = new TableWritingContext();
        CellPointer cellPointer = new CellPointer();
        WritingContext writingContext = new WritingContext(tableWritingContext, cellPointer);

        tableWritingContext.setWritingTable(true);
        tableWritingContext.setCurrentTableHeaders(new WrittenTableHeaders(0, List.of(new WrittenTableHeader("test", 1))));
        cellPointer.setCurrentColumn(1);

        String currentColumnName = writingContext.getCurrentColumnName();
        assertEquals("test", currentColumnName);
    }

    @Test
    public void currentColumnEmptyWhenHeadersSpecifiedAndInNotWritingState() {
        TableWritingContext tableWritingContext = new TableWritingContext();
        CellPointer cellPointer = new CellPointer();
        WritingContext writingContext = new WritingContext(tableWritingContext, cellPointer);

        tableWritingContext.setWritingTable(false);
        tableWritingContext.setCurrentTableHeaders(new WrittenTableHeaders(0, List.of(new WrittenTableHeader("test", 1))));
        cellPointer.setCurrentColumn(1);

        String currentColumnName = writingContext.getCurrentColumnName();
        assertEquals("", currentColumnName);
    }
}
