package com.mihai.dynamiccolumns;

import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.detector.ColumnDetector;
import com.mihai.reader.MaybeDynamicColumn;

public class PopulationRowDynamicColumnDetector implements ColumnDetector {

    @Override
    public boolean test(ReadingContext context, ReadableCell columnCell) {
        MaybeDynamicColumn column = new MaybeDynamicColumn(context, columnCell);
        return column.isAfter("country");
    }
}
