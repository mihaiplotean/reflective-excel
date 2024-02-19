package com.mihai.multipletables.destination;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.detector.ColumnDetector;
import com.mihai.reader.detector.ColumnDetectors;
import com.mihai.reader.workbook.sheet.ReadableCell;

public class DestinationHeaderColumnDetector implements ColumnDetector {

    private final ColumnDetector DELEGATE = ColumnDetectors.cellValuesInOrder("destination", "required", "delivered");

    @Override
    public boolean test(ReadingContext context, ReadableCell columnCell) {
        return DELEGATE.test(context, columnCell);
    }
}
