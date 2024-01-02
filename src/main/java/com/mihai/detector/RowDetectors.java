package com.mihai.detector;

import com.mihai.RowCells;

public class RowDetectors {

    private RowDetectors() {
        throw new IllegalStateException("Utility class");
    }

    public RowDetector allCellsEmpty() {
        return RowCells::allEmpty;
    }

    public RowDetector alwaysTrue() {
        return rowCells -> true;
    }
}
