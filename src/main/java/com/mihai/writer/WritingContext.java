package com.mihai.writer;

import com.mihai.core.CellPointer;
import com.mihai.writer.table.TableWritingContext;
import com.mihai.writer.table.WrittenTable;
import com.mihai.writer.table.WrittenTableHeaders;

/**
 * Provides information about the state of the writer.
 */
public class WritingContext {

    private final TableWritingContext tableContext;
    private final CellPointer cellPointer;

    public WritingContext(TableWritingContext tableContext, CellPointer cellPointer) {
        this.tableContext = tableContext;
        this.cellPointer = cellPointer;
    }

    /**
     * Provides the details of a table which has already been written to the sheet.
     *
     * @param tableId the id of the table.
     * @return details about a table which has already been written.
     * @see com.mihai.core.annotation.TableId
     */
    public WrittenTable getTable(String tableId) {
        return tableContext.getTable(tableId);
    }

    /**
     * Provides the details of the last table which has been written to the sheet.
     *
     * @return details about the last written table.
     */
    public WrittenTable getLastWrittenTable() {
        return tableContext.getLastWrittenTable();
    }

    /**
     * Returns the current sheet row that the writer is writing to.
     *
     * @return row index in the sheet.
     */
    public int getCurrentRow() {
        return cellPointer.getCurrentRow();
    }

    /**
     * Returns the current sheet column that the writer is writing to.
     *
     * @return column index in the sheet.
     */
    public int getCurrentColumn() {
        return cellPointer.getCurrentColumn();
    }

    /**
     * Returns the current column name in the table that is being written.
     *
     * @return the column name.
     */
    public String getCurrentColumnName() {
        WrittenTableHeaders headers = tableContext.getCurrentTableHeaders();
        if (headers == null) {
            return "";
        }
        return headers.getColumnName(cellPointer.getCurrentColumn());
    }

    /**
     * Compared to {@link #getCurrentRow()}, this returns the row number within the table, opposed to the
     * sheet row number. The first row is located right after the table header, no matter where in the sheet the
     * table is located.
     *
     * @return the row index in the table.
     */
    public int getCurrentTableRow() {
        return tableContext.getCurrentTableRow();
    }

    /**
     * Compared to {@link #getCurrentColumn()}, this returns the column number within the table, opposed to the
     * sheet column number. The first column is represented by the first table column, no matter where in the sheet the
     * table is located.
     *
     * @return the column index in the table.
     */
    public int getCurrentTableColumn() {
        return tableContext.getCurrentTableColumn();
    }
}
