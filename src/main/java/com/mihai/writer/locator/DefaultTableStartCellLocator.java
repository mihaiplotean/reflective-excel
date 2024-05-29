package com.mihai.writer.locator;

import com.mihai.core.workbook.CellLocation;
import com.mihai.writer.WritingContext;
import com.mihai.writer.table.WrittenTable;

public class DefaultTableStartCellLocator implements TableStartCellLocator {

    @Override
    public CellLocation getStartingCell(WritingContext context, String tableId) {
        WrittenTable table = context.getLastWrittenTable();
        if (table == null) {  // first table being written
            return new CellLocation(0, 0);
        }
        return table.getBottomLeftLocation().getDownBy(2);
    }
}
