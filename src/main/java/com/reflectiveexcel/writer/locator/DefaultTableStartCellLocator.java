package com.reflectiveexcel.writer.locator;

import com.reflectiveexcel.core.workbook.CellLocation;
import com.reflectiveexcel.writer.WritingContext;
import com.reflectiveexcel.writer.table.WrittenTable;

/**
 * The first table is written at cell A1. The following ones are written 2 rows down after each other.
 */
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
