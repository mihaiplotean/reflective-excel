package com.mihai.dynamiccolumns;

import com.mihai.PropertyCell;
import com.mihai.ReadingContext;
import com.mihai.RowCells;
import com.mihai.detector.DynamicColumnDetector;
import com.mihai.MaybeDynamicColumn;

public class PopulationRowDynamicColumnDetector implements DynamicColumnDetector {

    @Override
    public boolean isDynamic(ReadingContext context, PropertyCell headerCell) {
        MaybeDynamicColumn column = new MaybeDynamicColumn(context, headerCell);
        return column.isAfter("country");
    }
}
