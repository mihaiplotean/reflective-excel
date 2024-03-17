package com.mihai.writer;

import com.mihai.writer.table.CellWritingContext;
import com.mihai.writer.table.TableWritingContext;
import com.mihai.writer.table.WrittenTable;

public class WritingContext {

    private final TableWritingContext tableContext;
    private final CellWritingContext cellContext;

    public WritingContext(TableWritingContext tableContext, CellWritingContext cellContext) {
        this.tableContext = tableContext;
        this.cellContext = cellContext;
    }

    public WrittenTable getTable(String tableId) {
        return tableContext.getTable(tableId);
    }

    public WrittenTable getLastWrittenTable() {
        return tableContext.getLastWrittenTable();
    }

    public int getCurrentRow() {
        if(tableContext.isWritingTable()) {
            return tableContext.getCurrentRow();
        }
        return cellContext.getCurrentRow();
    }

    public int getCurrentColumn() {
        if(tableContext.isWritingTable()) {
            return tableContext.getCurrentColumn();
        }
        return cellContext.getCurrentColumn();
    }

    public String getCurrentColumnName() {
        return tableContext.getCurrentColumnName();
    }

    public int getCurrentTableRow() {
        return tableContext.getCurrentTableRow();
    }

    public int getCurrentTableColumn() {
        return tableContext.getCurrentTableColumn();
    }
}
