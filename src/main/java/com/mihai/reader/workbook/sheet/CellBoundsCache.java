package com.mihai.reader.workbook.sheet;

import com.mihai.writer.locator.CellLocation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

public class CellBoundsCache {

    private final Sheet sheet;
    private final Map<CellLocation, BoundedCell> cellLocationToBoundsMap = new HashMap<>();

    public CellBoundsCache(Sheet sheet) {
        this.sheet = sheet;
    }

    public BoundedCell getCellBounds(Cell cell) {
        CellLocation cellReference = new CellLocation(cell.getRowIndex(), cell.getColumnIndex());
        BoundedCell boundedCell = cellLocationToBoundsMap.get(cellReference);
        if (boundedCell != null) {
            return boundedCell;
        }
        CellRangeAddress region = getRegion(cell);
        if (region == null) {
            boundedCell = new BoundedCell(cell);
            cellLocationToBoundsMap.put(cellReference, boundedCell);
            return boundedCell;
        }
        return getBoundsOfRegion(region);
    }

    private CellRangeAddress getRegion(Cell cell) {
        for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
            if (mergedRegion.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                return mergedRegion;
            }
        }
        return null;
    }

    private BoundedCell getBoundsOfRegion(CellRangeAddress region) {
        Cell firstCellOfRegion = getCell(region.getFirstRow(), region.getFirstColumn());
        BoundedCell boundedCell = new BoundedCell(firstCellOfRegion,
                region.getFirstRow(), region.getFirstColumn(), region.getLastRow(), region.getLastColumn());
        for (int row = region.getFirstRow(); row <= region.getLastRow(); row++) {
            for (int column = region.getFirstColumn(); column <= region.getLastColumn(); column++) {
                cellLocationToBoundsMap.put(new CellLocation(row, column), boundedCell);
            }
        }
        return boundedCell;
    }

    private Cell getCell(int rowIndex, int columnIndex) {
        return Optional.ofNullable(sheet.getRow(rowIndex))
                .map(row -> row.getCell(columnIndex))
                .orElse(null);
    }
}
