package com.mihai.writer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class WritableRow {

    private final Row row;

    public WritableRow(Row row) {
        this.row = row;
    }

    public Cell getOrCreateCell(int columnNumber) {
        Cell cell = row.getCell(columnNumber);
        if(cell == null) {
            return row.createCell(columnNumber);
        }
        return cell;
    }
}
