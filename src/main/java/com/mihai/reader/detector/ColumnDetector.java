package com.mihai.reader.detector;

import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.ReadingContext;

public interface ColumnDetector {

    boolean test(ReadingContext context, ReadableCell columnCell);
}
