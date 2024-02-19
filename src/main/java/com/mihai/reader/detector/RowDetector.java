package com.mihai.reader.detector;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.workbook.sheet.ReadableRow;

public interface RowDetector {

    boolean test(ReadingContext context, ReadableRow row);

    default RowDetector and(RowDetector other) {
        return (context, rowCells) -> test(context, rowCells) && other.test(context, rowCells);
    }
}
