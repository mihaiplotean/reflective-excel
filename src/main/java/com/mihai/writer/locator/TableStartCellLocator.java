package com.mihai.writer.locator;

import com.mihai.core.workbook.CellLocation;
import com.mihai.writer.WritingContext;

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
     * @see com.mihai.core.annotation.TableId
     */
    CellLocation getStartingCell(WritingContext context, String tableId);
}
