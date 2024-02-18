package com.mihai.writer;

import com.mihai.writer.serializer.WritableCellStyle;
import org.apache.poi.ss.formula.BaseFormulaEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

public class WritableSheet {

    private final Sheet sheet;
    private final CellStyleCreator cellStyleCreator;

    public WritableSheet(Sheet sheet) {
        this.sheet = sheet;
        this.cellStyleCreator = new CellStyleCreator(sheet.getWorkbook());
    }
//
//    public Row createNextRow() {
//        Row row = sheet.createRow(currentRow);
//        currentRow++;
//        return row;
//    }

    public Row getRow(int rowNumber) {
        Row row = sheet.getRow(rowNumber);
        if (row == null) {
            return sheet.createRow(rowNumber);
        }
        return row;
    }

    public void writeCell(WritableCell writableCell) {
        WritableRow startRow = getOrCreateRow(writableCell.getStartRow());
        Cell cell = startRow.getOrCreateCell(writableCell.getStartColumn());
        writableCell.writeTo(cell);
        WritableCellStyle style = writableCell.getStyle();
        if(style != null) {
            cell.setCellStyle(cellStyleCreator.getCellStyle(style));
        }
        mergeCellBounds(cell, writableCell);
    }

    private WritableRow getOrCreateRow(int rowNumber) {
        Row row = sheet.getRow(rowNumber);
        if (row == null) {
            return new WritableRow(sheet.createRow(rowNumber));
        }
        return new WritableRow(row);
    }

    private void mergeCellBounds(Cell cell, WritableCell writableCell) {
        int startRow = writableCell.getStartRow();
        int endRow = writableCell.getEndRow();
        int startColumn = writableCell.getStartColumn();
        int endColumn = writableCell.getEndColumn();
        if (endRow - startRow > 0 || endColumn - startColumn > 0) {
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startColumn, endColumn));
            CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
        }
    }

    public void evaluateAllFormulas() {
        BaseFormulaEvaluator.evaluateAllFormulaCells(sheet.getWorkbook());
    }

    public void autoResizeAllColumns() {
        for (Row row : sheet) {
            for (Cell cell : row) {
                int columnIndex = cell.getColumnIndex();
                int originalColumnWidth = sheet.getColumnWidth(columnIndex);
                sheet.autoSizeColumn(columnIndex);
                if(originalColumnWidth > sheet.getColumnWidth(columnIndex)) {
                    sheet.setColumnWidth(columnIndex, originalColumnWidth);
                }
            }
            return;
        }
    }
}
