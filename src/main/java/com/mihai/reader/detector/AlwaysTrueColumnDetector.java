package com.mihai.reader.detector;

import com.mihai.reader.ReadingContext;

public class AlwaysTrueColumnDetector implements ColumnDetector {

    @Override
    public boolean test(ReadingContext context, MaybeDynamicColumn column) {
        return true;
    }
}
