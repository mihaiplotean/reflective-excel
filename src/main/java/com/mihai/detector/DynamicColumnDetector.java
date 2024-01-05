package com.mihai.detector;

import com.mihai.MaybeDynamicColumn;
import com.mihai.PropertyCell;
import com.mihai.ReadingContext;

public interface DynamicColumnDetector {

    boolean isDynamic(ReadingContext context, PropertyCell headerCell);
}
