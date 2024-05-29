package com.mihai.writer;

import com.mihai.core.CellPointer;
import com.mihai.writer.table.TableWritingContext;
import com.mihai.writer.table.WrittenTable;
import com.mihai.writer.table.WrittenTableHeaders;

public class WritingContext {

    private final TableWritingContext tableContext;
    private final CellPointer cellPointer;

    public WritingContext(TableWritingContext tableContext, CellPointer cellPointer) {
        this.tableContext = tableContext;
        this.cellPointer = cellPointer;
    }

    public WrittenTable getTable(String tableId) {
        return tableContext.getTable(tableId);
    }

    public WrittenTable getLastWrittenTable() {
        return tableContext.getLastWrittenTable();
    }

    public int getCurrentRow() {
        return cellPointer.getCurrentRow();
    }

    public int getCurrentColumn() {
        return cellPointer.getCurrentColumn();
    }

    public String getCurrentColumnName() {
        WrittenTableHeaders headers = tableContext.getCurrentTableHeaders();
        if(headers == null) {
            return "";
        }
        return headers.getColumnName(cellPointer.getCurrentColumn());
    }

    public int getCurrentTableRow() {
        return tableContext.getCurrentTableRow();
    }

    public int getCurrentTableColumn() {
        return tableContext.getCurrentTableColumn();
    }
}
