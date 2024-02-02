package com.mihai.multipletables.supplier;

import com.mihai.ReadingContext;
import com.mihai.detector.ColumnDetector;
import com.mihai.detector.ColumnDetectors;
import com.mihai.workbook.sheet.PropertyCell;

public class SupplierHeaderColumnDetector implements ColumnDetector {

    private final ColumnDetector DELEGATE = ColumnDetectors.cellValuesInOrder(
            "supplier",
            "capacity"
    );

    @Override
    public boolean test(ReadingContext context, PropertyCell columnCell) {
        return DELEGATE.test(context, columnCell);
    }
}
