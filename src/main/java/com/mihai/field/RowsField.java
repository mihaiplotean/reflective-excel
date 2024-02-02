package com.mihai.field;

import com.mihai.ExcelReadingSettings;
import com.mihai.ReflectionUtilities;
import com.mihai.RowReader;
import com.mihai.detector.ColumnDetector;
import com.mihai.detector.RowDetector;
import com.mihai.detector.UseAsSpecifiedInReadSettings;
import com.mihai.field.value.AnnotatedFieldValue;
import com.mihai.field.value.RowsFieldValue;

import java.lang.reflect.Field;

public class RowsField implements AnnotatedField {

    private final RowReader rowReader;
    private final Field field;

    private final Class<? extends RowDetector> lastRowDetectorType;
    private final Class<? extends RowDetector> headerRowDetectorType;
    private final Class<? extends RowDetector> skipRowDetectorType;
    private final Class<? extends ColumnDetector> headerStartColumnDetectorType;
    private final Class<? extends ColumnDetector> headerLastColumnDetectorType;

    public RowsField(RowReader rowReader, Field field,
                     Class<? extends RowDetector> lastRowDetectorType,
                     Class<? extends RowDetector> headerRowDetectorType,
                     Class<? extends RowDetector> skipRowDetectorType,
                     Class<? extends ColumnDetector> headerStartColumnDetectorType,
                     Class<? extends ColumnDetector> headerLastColumnDetectorType) {
        this.rowReader = rowReader;
        this.field = field;
        this.lastRowDetectorType = lastRowDetectorType;
        this.headerRowDetectorType = headerRowDetectorType;
        this.skipRowDetectorType = skipRowDetectorType;
        this.headerStartColumnDetectorType = headerStartColumnDetectorType;
        this.headerLastColumnDetectorType = headerLastColumnDetectorType;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldValue newFieldValue() {
        ExcelReadingSettings settings = rowReader.getSettings();
        RowDetector lastRowDetector = initializeDetector(lastRowDetectorType, settings.getLastRowDetector());
        RowDetector headerRowDetector = initializeDetector(headerRowDetectorType, settings.getHeaderRowDetector());
        RowDetector skipRowDetector = initializeDetector(skipRowDetectorType, settings.getSkipRowDetector());
        ColumnDetector headerStartColumnDetector = initializeDetector(headerStartColumnDetectorType, settings.getHeaderStartColumnDetector());
        ColumnDetector headerLastColumnDetector = initializeDetector(headerLastColumnDetectorType, settings.getHeaderLastColumnDetector());
        return new RowsFieldValue(rowReader, field,
                lastRowDetector, headerRowDetector, skipRowDetector, headerStartColumnDetector, headerLastColumnDetector);
    }

    private <T> T initializeDetector(Class<? extends T> clazz, T defaultValue) {
        if(clazz == UseAsSpecifiedInReadSettings.class) {
            return defaultValue;
        }
        return ReflectionUtilities.newObject(clazz);
    }
}
