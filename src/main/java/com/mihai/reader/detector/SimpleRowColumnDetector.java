package com.mihai.reader.detector;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.util.CellReference;

/**
 * Denotes that the table header starts at the provided cell. The headers end when an empty header is encountered.
 * The table rows end when an empty row is encountered.
 */
public class SimpleRowColumnDetector implements TableRowColumnDetector {

    private final String headerStartCellReference;

    /**
     * @param headerStartCellReference the reference to the table's first header cell. Example: "A1", "B2".
     */
    public SimpleRowColumnDetector(String headerStartCellReference) {
        this.headerStartCellReference = headerStartCellReference;
    }

    @Override
    public boolean isLastRow(ReadingContext context, ReadableRow row) {
        return isNextRowEmpty(context, row);
    }

    private static boolean isNextRowEmpty(ReadingContext context, ReadableRow row) {
        return context.getCurrentTableRow(row.getRowNumber() + 1).isEmpty();
    }

    @Override
    public boolean isHeaderRow(ReadingContext context, ReadableRow row) {
        return row.getRowNumber() == new CellReference(headerStartCellReference).getRow();
    }

    @Override
    public boolean shouldSkipRow(ReadingContext context, ReadableRow tableRow) {
        return false;
    }

    @Override
    public boolean isHeaderStartColumn(ReadingContext context, ReadableCell cell) {
        return cell.getColumnNumber() == new CellReference(headerStartCellReference).getCol();
    }

    @Override
    public boolean isHeaderLastColumn(ReadingContext context, ReadableCell cell) {
        return isColumnAfterHeaderEmpty(context, cell);
    }

    private static boolean isColumnAfterHeaderEmpty(ReadingContext context, ReadableCell cell) {
        int rowNumber = cell.getRowNumber();
        return StringUtils.isEmpty(context.getCellValue(rowNumber, cell.getEndColumn() + 1));
    }
}
