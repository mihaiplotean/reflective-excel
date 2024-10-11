package com.reflectiveexcel.writer.node;

public class ColumnSizePreferences {

    public static final int MIN_WIDTH = 4;
    public static final int MAX_WIDTH = 256;

    public static final ColumnSizePreferences DEFAULT = new ColumnSizePreferences(MAX_WIDTH, -1, MIN_WIDTH);

    private final int maxSize;
    private final int preferredSize;
    private final int minSize;

    public ColumnSizePreferences(int maxSize, int preferredSize, int minSize) {
        this.maxSize = maxSize;
        this.preferredSize = preferredSize;
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getPreferredSize() {
        return preferredSize;
    }

    public int getMinSize() {
        return minSize;
    }
}
