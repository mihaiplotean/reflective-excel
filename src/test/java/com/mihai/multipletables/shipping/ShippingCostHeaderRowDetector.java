package com.mihai.multipletables.shipping;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.detector.RowDetector;
import com.mihai.reader.detector.RowDetectors;
import com.mihai.reader.workbook.sheet.ReadableRow;

public class ShippingCostHeaderRowDetector implements RowDetector {

    private static final RowDetector DELEGATE = RowDetectors.hasAllValues("supplier", "destination", "units shipped");

    @Override
    public boolean test(ReadingContext context, ReadableRow row) {
        return DELEGATE.test(context, row);
    }
}
