package com.mihai.reader.detector;

import com.mihai.reader.ExcelReadingSettings;

/**
 * Dummy interface used to emphasize that the {@link RowDetector} or {@link ColumnDetector} should be picked
 * up as specified in {@link ExcelReadingSettings}.
 * Not to be used outside this library.
 */
public interface UseAsSpecifiedInReadSettings extends RowDetector, ColumnDetector {
}
