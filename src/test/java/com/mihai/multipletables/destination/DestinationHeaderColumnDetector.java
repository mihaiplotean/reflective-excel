package com.mihai.multipletables.destination;

import com.mihai.ReadingContext;
import com.mihai.detector.ColumnDetector;
import com.mihai.detector.ColumnDetectors;
import com.mihai.workbook.sheet.PropertyCell;

public class DestinationHeaderColumnDetector implements ColumnDetector {

    private final ColumnDetector DELEGATE = ColumnDetectors.cellValuesInOrder("destination", "required", "delivered");

    @Override
    public boolean test(ReadingContext context, PropertyCell columnCell) {
        return DELEGATE.test(context, columnCell);
    }
}
