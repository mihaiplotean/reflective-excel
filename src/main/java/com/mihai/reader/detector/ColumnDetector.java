package com.mihai.reader.detector;

import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.ReadingContext;

public interface ColumnDetector {

    boolean test(ReadingContext context, ReadableCell columnCell);

    default ColumnDetector and(ColumnDetector other) {
        return (context, columnCell) -> test(context, columnCell) && other.test(context, columnCell);
    }
}
