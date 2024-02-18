package com.mihai.detector;

import com.mihai.ReadingContext;
import com.mihai.workbook.sheet.ReadableRow;

public interface RowDetector {

    boolean test(ReadingContext context, ReadableRow row);

    default RowDetector and(RowDetector other) {
        return (context, rowCells) -> test(context, rowCells) && other.test(context, rowCells);
    }
}
