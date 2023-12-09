package com.mihai;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.math3.util.Pair;

import java.util.Map;

public interface DynamicColumnDetector {

    boolean isDynamicColumn(MaybeDynamicColumn column);
}
