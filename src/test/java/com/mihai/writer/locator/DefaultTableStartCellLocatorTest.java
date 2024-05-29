package com.mihai.writer.locator;

import com.mihai.core.CellPointer;
import com.mihai.core.workbook.CellLocation;
import com.mihai.core.workbook.Bounds;
import com.mihai.writer.WritingContext;
import com.mihai.writer.table.TableWritingContext;
import com.mihai.writer.table.WrittenTable;
import com.mihai.writer.table.WrittenTableHeaders;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultTableStartCellLocatorTest {

    @Test
    public void firstTableIsLocatedAtCellA1() {
        WritingContext writingContext = new WritingContext(new TableWritingContext(), new CellPointer());
        DefaultTableStartCellLocator startCellLocator = new DefaultTableStartCellLocator();

        CellLocation tableStartCell = startCellLocator.getStartingCell(writingContext, "");
        assertEquals("A1", tableStartCell.getReference());
    }

    @Test
    public void eachNextTableIsLocatedTwoRowsBellow() {
        TableWritingContext tableContext = new TableWritingContext();
        WritingContext writingContext = new WritingContext(tableContext, new CellPointer());
        DefaultTableStartCellLocator startCellLocator = new DefaultTableStartCellLocator();

        tableContext.appendTable(new WrittenTable("table 1", dummyHeaders(), new Bounds(0, 0, 10, 10)));
        assertEquals(12, startCellLocator.getStartingCell(writingContext, "table 2").row());

        tableContext.appendTable(new WrittenTable("table 2", dummyHeaders(), new Bounds(12, 0, 20, 0)));
        assertEquals(22, startCellLocator.getStartingCell(writingContext, "table 3").row());
    }

    private static WrittenTableHeaders dummyHeaders() {
        return new WrittenTableHeaders(0, List.of());
    }
}