package com.mihai.detector;

import com.mihai.ReadingContext;
import com.mihai.RowCells;

public interface RowDetector {

    boolean test(RowCells rowCells);
}
