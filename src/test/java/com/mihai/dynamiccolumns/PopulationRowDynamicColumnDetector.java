package com.mihai.dynamiccolumns;

import com.mihai.detector.DynamicColumnDetector;
import com.mihai.MaybeDynamicColumn;

public class PopulationRowDynamicColumnDetector implements DynamicColumnDetector {

    @Override
    public boolean isDynamicColumn(MaybeDynamicColumn column) {
        return column.isAfter("country");
    }
}
