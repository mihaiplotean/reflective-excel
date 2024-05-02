package com.mihai.reader.detector;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.util.CellReference;

public class SimpleRowColumnDetector implements RowColumnDetector2 {

    private final String headerStartCellReference;

    public SimpleRowColumnDetector(String headerStartCellReference) {
        this.headerStartCellReference = headerStartCellReference;
    }

    @Override
    public boolean isLastRow(ReadingContext context, ReadableRow row) {
        return isNextRowEmpty(context, row);
    }

    private static boolean isNextRowEmpty(ReadingContext context, ReadableRow row) {
        return context.getRow(row.getRowNumber() + 1).isEmpty();
    }

    @Override
    public boolean isHeaderRow(ReadingContext context, ReadableRow row) {
        return row.getRowNumber() == new CellReference(headerStartCellReference).getRow();
    }

    @Override
    public boolean shouldSkipRow(ReadingContext context, ReadableRow row) {
        return false;
    }

    @Override
    public boolean isHeaderStartColumn(ReadingContext context, ReadableCell cell) {
        return cell.getColumnNumber() == new CellReference(headerStartCellReference).getCol();
    }

    @Override
    public boolean isHeaderLastColumn(ReadingContext context, ReadableCell cell) {
        return isNextColumnEmpty(context, cell);
    }

    private static boolean isNextColumnEmpty(ReadingContext context, ReadableCell cell) {
        int rowNumber = cell.getRowNumber();
        int columnNumber = cell.getColumnNumber();
        return StringUtils.isEmpty(context.getCellValue(rowNumber, columnNumber + 1));
    }
}
