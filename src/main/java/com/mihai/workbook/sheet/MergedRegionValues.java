package com.mihai.workbook.sheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

public class MergedRegionValues {

    private final Sheet sheet;
    private final List<CellRangeAddress> mergedRegions;
    private final Map<CellCoordinates, CellBounds> cellBoundsMap = new HashMap<>();

    public MergedRegionValues(Sheet sheet) {
        this.sheet = sheet;
        this.mergedRegions = sheet.getMergedRegions();
    }

    public CellBounds getCellBounds(Cell cell) {
        CellCoordinates cellReference = new CellCoordinates(cell.getRowIndex(), cell.getColumnIndex());
        CellBounds cellBounds = cellBoundsMap.get(cellReference);
        if (cellBounds != null) {
            return cellBounds;
        }
        CellRangeAddress region = getRegion(cell);
        if (region == null) {
            cellBounds = new CellBounds(cell);
            cellBoundsMap.put(cellReference, cellBounds);
            return cellBounds;
        }
        return getBoundsOfRegion(region);
    }

    private CellRangeAddress getRegion(Cell cell) {
        for (CellRangeAddress mergedRegion : mergedRegions) {
            if (mergedRegion.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                return mergedRegion;
            }
        }
        return null;
    }

    private CellBounds getBoundsOfRegion(CellRangeAddress region) {
        Cell firstCellOfRegion = getCell(region.getFirstRow(), region.getFirstColumn());
        CellBounds cellBounds = new CellBounds(firstCellOfRegion,
                region.getFirstRow(), region.getFirstColumn(), region.getLastRow(), region.getLastColumn());
        for (int row = region.getFirstRow(); row <= region.getLastRow(); row++) {
            for (int column = region.getFirstColumn(); column <= region.getLastColumn(); column++) {
                cellBoundsMap.put(new CellCoordinates(row, column), cellBounds);
            }
        }
        return cellBounds;
    }

    private Cell getCell(int rowIndex, int columnIndex) {
        return Optional.ofNullable(sheet.getRow(rowIndex))
                .map(row -> row.getCell(columnIndex))
                .orElse(null);
    }

    private record CellCoordinates(int row, int column) {

    }
}
