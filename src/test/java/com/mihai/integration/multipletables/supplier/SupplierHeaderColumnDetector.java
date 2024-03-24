package com.mihai.integration.multipletables.supplier;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.detector.ColumnDetector;
import com.mihai.reader.detector.ColumnDetectors;
import com.mihai.reader.workbook.sheet.ReadableCell;

public class SupplierHeaderColumnDetector implements ColumnDetector {

    private final ColumnDetector DELEGATE = ColumnDetectors.cellValuesInOrder(
            "supplier",
            "capacity"
    );

    @Override
    public boolean test(ReadingContext context, ReadableCell columnCell) {
        return DELEGATE.test(context, columnCell);
    }
}
