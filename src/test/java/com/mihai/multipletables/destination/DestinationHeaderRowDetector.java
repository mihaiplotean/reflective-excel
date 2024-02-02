package com.mihai.multipletables.destination;

import com.mihai.ReadingContext;
import com.mihai.detector.RowDetector;
import com.mihai.detector.RowDetectors;
import com.mihai.workbook.sheet.RowCells;

public class DestinationHeaderRowDetector implements RowDetector {

    private static final RowDetector DELEGATE = RowDetectors.hasAllValues("destination", "required", "delivered");

    @Override
    public boolean test(ReadingContext context, RowCells rowCells) {
        return DELEGATE.test(context, rowCells);
    }
}
