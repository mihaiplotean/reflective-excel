package com.mihai.detector;

import com.mihai.ReadingContext;
import com.mihai.workbook.sheet.RowCells;

public interface RowDetector {

    boolean test(ReadingContext context, RowCells rowCells);

    default RowDetector and(RowDetector other) {
        return (context, rowCells) -> test(context, rowCells) && other.test(context, rowCells);
    }
}
