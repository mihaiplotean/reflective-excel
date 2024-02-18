package com.mihai.multipletables.shipping;

import com.mihai.ReadingContext;
import com.mihai.detector.ColumnDetector;
import com.mihai.detector.ColumnDetectors;
import com.mihai.workbook.sheet.ReadableCell;

public class ShippingCostHeaderColumnDetector implements ColumnDetector {

    private final ColumnDetector DELEGATE = ColumnDetectors.cellValuesInOrder(
            "supplier",
            "destination",
            "units shipped"
    );

    @Override
    public boolean test(ReadingContext context, ReadableCell columnCell) {
        return DELEGATE.test(context, columnCell);
    }
}
