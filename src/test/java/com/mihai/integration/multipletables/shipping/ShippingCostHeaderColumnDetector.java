package com.mihai.integration.multipletables.shipping;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.detector.ColumnDetector;
import com.mihai.reader.detector.ColumnDetectors;
import com.mihai.reader.workbook.sheet.ReadableCell;

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
