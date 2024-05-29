package com.mihai.reader.workbook.sheet;

import com.mihai.core.workbook.Bounds;
import com.mihai.reader.CellValueFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

public class MergedCellsFinder {

    private final Sheet sheet;
    private final CellValueFormatter cellValueFormatter;

    public MergedCellsFinder(Sheet sheet, CellValueFormatter cellValueFormatter) {
        this.sheet = sheet;
        this.cellValueFormatter = cellValueFormatter;
    }

    public List<MergedCell> getMergedCellsIntersectingRow(int row) {
        return sheet.getMergedRegions().stream()
                .filter(mergedRegion -> mergedRegion.containsRow(row))
                .map(this::createMergedCell)
                .toList();
    }

    private MergedCell createMergedCell(CellRangeAddress region) {
        Cell firstCellOfRegion = getCell(region.getFirstRow(), region.getFirstColumn());
        Objects.requireNonNull(firstCellOfRegion, "Top left cell of a merged region cannot be null!");
        return new MergedCell(firstCellOfRegion, cellValueFormatter.toString(firstCellOfRegion),
                new Bounds(region.getFirstRow(), region.getFirstColumn(), region.getLastRow(), region.getLastColumn()));
    }

    private Cell getCell(int rowIndex, int columnIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            return null;
        }
        return row.getCell(columnIndex);
    }
}
