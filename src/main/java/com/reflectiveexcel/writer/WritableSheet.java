package com.reflectiveexcel.writer;

import org.apache.poi.ss.formula.BaseFormulaEvaluator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;

public class WritableSheet {

    private final Sheet sheet;
    private final CellStyleCreator cellStyleCreator;

    public WritableSheet(Sheet sheet) {
        this.sheet = sheet;
        this.cellStyleCreator = new CellStyleCreator(sheet.getWorkbook());
    }

    public Sheet getSheet() {
        return sheet;
    }

    public Workbook getWorkbook() {
        return sheet.getWorkbook();
    }

    public CellStyleCreator getCellStyleCreator() {
        return cellStyleCreator;
    }

    public Cell writeCell(WritableCell writableCell) {
        WritableRow startRow = getOrCreateRow(writableCell.getStartRow());
        Cell cell = startRow.getOrCreateCell(writableCell.getStartColumn());
        writableCell.writeTo(cell);
        return cell;
    }

    private WritableRow getOrCreateRow(int rowNumber) {
        Row row = sheet.getRow(rowNumber);
        if (row == null) {
            return new WritableRow(sheet.createRow(rowNumber));
        }
        return new WritableRow(row);
    }

    public void mergeCellBounds(Cell cell, WritableCell writableCell) {
        int startRow = writableCell.getStartRow();
        int endRow = writableCell.getEndRow();
        int startColumn = writableCell.getStartColumn();
        int endColumn = writableCell.getEndColumn();
        if (endRow - startRow > 0 || endColumn - startColumn > 0) {
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startColumn, endColumn));
            CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
        }
    }

    public void applyRegionStyle(CellRangeAddress rangeAddress, CellStyle style) {
        BorderStyle borderTop = style.getBorderTop();
        if (borderTop != null) {
            RegionUtil.setBorderTop(borderTop, rangeAddress, sheet);
        }
        BorderStyle borderRight = style.getBorderRight();
        if (borderRight != null) {
            RegionUtil.setBorderRight(borderRight, rangeAddress, sheet);
        }
        BorderStyle borderBottom = style.getBorderBottom();
        if (borderTop != null) {
            RegionUtil.setBorderBottom(borderBottom, rangeAddress, sheet);
        }
        BorderStyle borderLeft = style.getBorderLeft();
        if (borderLeft != null) {
            RegionUtil.setBorderBottom(borderLeft, rangeAddress, sheet);
        }
        RegionUtil.setTopBorderColor(style.getTopBorderColor(), rangeAddress, sheet);
        RegionUtil.setRightBorderColor(style.getRightBorderColor(), rangeAddress, sheet);
        RegionUtil.setBottomBorderColor(style.getBottomBorderColor(), rangeAddress, sheet);
        RegionUtil.setLeftBorderColor(style.getLeftBorderColor(), rangeAddress, sheet);
    }

    public void evaluateAllFormulas() {
        BaseFormulaEvaluator.evaluateAllFormulaCells(sheet.getWorkbook());
    }

    public int autoSizeColumnWidth(int columnIndex) {
        sheet.autoSizeColumn(columnIndex);
        return sheet.getColumnWidth(columnIndex);
    }

    public void setColumnWidth(int columnIndex, int width) {
        sheet.setColumnWidth(columnIndex, width);
    }

    public void installFiltering(int row, int startColumn, int endColumn) {
        sheet.setAutoFilter(new CellRangeAddress(row, row, startColumn, endColumn));
    }
}
