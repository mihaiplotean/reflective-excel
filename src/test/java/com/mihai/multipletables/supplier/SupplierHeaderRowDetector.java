package com.mihai.multipletables.supplier;

import com.mihai.ReadingContext;
import com.mihai.detector.RowDetector;
import com.mihai.detector.RowDetectors;
import com.mihai.workbook.sheet.ReadableRow;

public class SupplierHeaderRowDetector implements RowDetector {

    private static final RowDetector DELEGATE = RowDetectors.hasAllValues("supplier", "capacity");

    @Override
    public boolean test(ReadingContext context, ReadableRow row) {
        return DELEGATE.test(context, row);
    }
}
