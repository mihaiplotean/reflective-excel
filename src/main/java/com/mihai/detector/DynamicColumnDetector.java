package com.mihai.detector;

import com.mihai.MaybeDynamicColumn;

public interface DynamicColumnDetector {

    boolean isDynamicColumn(MaybeDynamicColumn column);
}
