package com.mihai.multipletables.shipping;

import com.mihai.ReadingContext;
import com.mihai.detector.RowDetector;
import com.mihai.detector.RowDetectors;
import com.mihai.workbook.sheet.RowCells;

public class ShippingCostHeaderRowDetector implements RowDetector {

    private static final RowDetector DELEGATE = RowDetectors.hasAllValues("supplier", "destination", "units shipped");

    @Override
    public boolean test(ReadingContext context, RowCells rowCells) {
        return DELEGATE.test(context, rowCells);
    }
}
