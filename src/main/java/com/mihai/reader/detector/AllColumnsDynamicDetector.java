package com.mihai.reader.detector;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.workbook.sheet.ReadableCell;

public class AllColumnsDynamicDetector implements ColumnDetector {

    @Override
    public boolean test(ReadingContext context, ReadableCell columnCell) {
        return true;
    }
}
