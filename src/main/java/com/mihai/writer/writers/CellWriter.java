package com.mihai.writer.writers;

import java.util.List;

import com.mihai.core.workbook.CellLocation;
import com.mihai.writer.WritableCell;
import com.mihai.writer.WritableSheet;
import com.mihai.writer.style.WritableCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

public class CellWriter {

    private final WritableSheet sheet;

    private int offsetRows;
    private int offsetColumns;

    public CellWriter(WritableSheet sheet) {
        this.sheet = sheet;
    }

    public void setOffSet(int offsetRows, int offsetColumns) {
        this.offsetRows = offsetRows;
        this.offsetColumns = offsetColumns;
    }

    public int getOffsetRows() {
        return offsetRows;
    }

    public int getOffsetColumns() {
        return offsetColumns;
    }

    public CellLocation writeCell(WritableCell cellReference, List<WritableCellStyle> styles) {
        WritableCell writableCell = applyOffset(cellReference);
        Cell cell = sheet.writeCell(writableCell);

        styles.stream()
                .reduce(WritableCellStyle::combineWith)
                .ifPresent(cellStyle -> applyCellStyle(cellStyle, cell, writableCell));

        if (cellReference.spansMultipleCells()) {
            sheet.mergeCellBounds(cell, writableCell);
        }

        return writableCell.getLocation();
    }

    private WritableCell applyOffset(WritableCell cell) {
        if (offsetRows == 0 && offsetColumns == 0) {
            return cell;
        }
        return new WritableCell(
                cell.getValue(),
                offsetRows + cell.getStartRow(),
                offsetColumns + cell.getStartColumn(),
                offsetRows + cell.getEndRow(),
                offsetColumns + cell.getEndColumn()
        );
    }

    private void applyCellStyle(WritableCellStyle cellStyle, Cell cell, WritableCell cellReference) {
        CellStyle style = sheet.getCellStyleCreator().getCellStyle(cellStyle);
        cell.setCellStyle(style);
        if (cellReference.spansMultipleCells()) {
            sheet.applyRegionStyle(cellReference.getCellRangeAddress(), style);
        }
    }
}
