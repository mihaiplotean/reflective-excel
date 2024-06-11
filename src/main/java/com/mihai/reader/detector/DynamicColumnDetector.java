package com.mihai.reader.detector;

import com.mihai.reader.ReadingContext;

/**
 * Specifies when a column can be considered dynamic. Dynamic columns are columns which do not always appear in a sheet.
 * @see com.mihai.core.annotation.DynamicColumns
 */
public interface DynamicColumnDetector {

    /**
     * Checks if the provided column is dynamic.
     *
     * @param context information related to the sheet's reading process.
     * @param column information related a cell in the header, which could correspond to a dynamic column.
     * @return true if the column is dynamic according to this column detector, and false otherwise.
     */
    boolean test(ReadingContext context, MaybeDynamicColumn column);
}
