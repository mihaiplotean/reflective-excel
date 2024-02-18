package com.mihai.detector;

import com.mihai.workbook.sheet.ReadableCell;
import com.mihai.ReadingContext;

public interface ColumnDetector {

    boolean test(ReadingContext context, ReadableCell columnCell);

    default ColumnDetector and(ColumnDetector other) {
        return (context, columnCell) -> test(context, columnCell) && other.test(context, columnCell);
    }
}
