package com.mihai.writer.locator;

import com.mihai.writer.WritingContext;

public interface TableStartCellLocator {

    CellLocation getStartingCell(WritingContext context, String tableId);
}
