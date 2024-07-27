package com.reflectiveexcel.writer.locator;

import com.reflectiveexcel.core.workbook.CellLocation;
import com.reflectiveexcel.writer.WritingContext;

/**
 * Used to specify on which cell the table starts.
 */
public interface TableStartCellLocator {

    /**
     * Returns the starting location of the table to be written.
     *
     * @param context information related to the sheet's writing process.
     * @param tableId the id of the table.
     * @return the starting location of the table.
     * @see com.reflectiveexcel.core.annotation.TableId
     */
    CellLocation getStartingCell(WritingContext context, String tableId);
}
