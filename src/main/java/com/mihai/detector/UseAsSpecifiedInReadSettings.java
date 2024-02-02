package com.mihai.detector;

/**
 * Dummy interface used to emphasize that the {@link RowDetector} or {@link ColumnDetector} should be picked
 * up as specified in {@link com.mihai.ExcelReadingSettings}.
 * Not to be used outside this library.
 */
public interface UseAsSpecifiedInReadSettings extends RowDetector, ColumnDetector {
}
