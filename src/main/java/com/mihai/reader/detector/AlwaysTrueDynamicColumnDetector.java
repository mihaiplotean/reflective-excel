package com.mihai.reader.detector;

import com.mihai.reader.ReadingContext;

/**
 * A column detector which considers all columns dynamic.
 */
public class AlwaysTrueDynamicColumnDetector implements DynamicColumnDetector {

    @Override
    public boolean test(ReadingContext context, MaybeDynamicColumn column) {
        return true;
    }
}
