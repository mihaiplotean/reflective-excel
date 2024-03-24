package com.mihai.integration.multipletables.destination;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.detector.RowDetector;
import com.mihai.reader.detector.RowDetectors;
import com.mihai.reader.workbook.sheet.ReadableRow;

public class DestinationHeaderRowDetector implements RowDetector {

    private static final RowDetector DELEGATE = RowDetectors.hasAllValues("destination", "required", "delivered");

    @Override
    public boolean test(ReadingContext context, ReadableRow row) {
        return DELEGATE.test(context, row);
    }
}
