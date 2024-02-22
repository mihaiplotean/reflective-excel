package com.mihai.writer;

import com.mihai.writer.style.WritableCellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.util.List;

public class CellWriter {

    private final WritableSheet sheet;
    private final CellStyleCreator cellStyleCreator;

    public CellWriter(WritableSheet sheet) {
        this.sheet = sheet;
        this.cellStyleCreator = new CellStyleCreator(sheet.getWorkbook());
    }

    public void writeCell(WritableCell cellReference) {
        writeCell(cellReference, List.of());
    }

    public void writeCell(WritableCell cellReference, WritableCellStyle style) {
        writeCell(cellReference, List.of(style));
    }

    public void writeCell(WritableCell cellReference, List<WritableCellStyle> styles) {
        Cell cell = sheet.writeCell(cellReference);

        styles.stream()
                .reduce(WritableCellStyle::combineWith)
                .ifPresent(cellStyle -> applyCellStyle(cellStyle, cell, cellReference));

        sheet.mergeCellBounds(cell, cellReference);
    }

    private void applyCellStyle(WritableCellStyle cellStyle, Cell cell, WritableCell cellReference) {
        CellStyle style = cellStyleCreator.getCellStyle(cellStyle);
        cell.setCellStyle(style);
        if(cellReference.spansMultipleCells()) {
            sheet.applyRegionStyle(cellReference.getCellRangeAddress(), style);
        }
    }
}
