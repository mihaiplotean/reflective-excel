package com.mihai.dynamiccolumns;

import com.mihai.workbook.PropertyCell;
import com.mihai.ReadingContext;
import com.mihai.detector.ColumnDetector;
import com.mihai.MaybeDynamicColumn;

public class PopulationRowDynamicColumnDetector implements ColumnDetector {

    @Override
    public boolean test(ReadingContext context, PropertyCell columnCell) {
        MaybeDynamicColumn column = new MaybeDynamicColumn(context, columnCell);
        return column.isAfter("country");
    }
}
