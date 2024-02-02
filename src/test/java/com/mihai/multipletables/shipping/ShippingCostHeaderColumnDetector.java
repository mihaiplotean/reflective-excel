package com.mihai.multipletables.shipping;

import com.mihai.ReadingContext;
import com.mihai.detector.ColumnDetector;
import com.mihai.detector.ColumnDetectors;
import com.mihai.workbook.sheet.PropertyCell;

public class ShippingCostHeaderColumnDetector implements ColumnDetector {

    private final ColumnDetector DELEGATE = ColumnDetectors.cellValuesInOrder(
            "supplier",
            "destination",
            "units shipped"
    );

    @Override
    public boolean test(ReadingContext context, PropertyCell columnCell) {
        return DELEGATE.test(context, columnCell);
    }
}
