package com.mihai.assertion;

import java.util.Collections;
import java.util.List;

public class ExcelAssertSettings {

    public static final ExcelAssertSettings DEFAULT = ExcelAssertSettings.builder().build();

    private final boolean caseSensitive;
    private final boolean compareCellStyles;
    private final List<String> sheetNames;
    private final int differencesStopCount;

    private ExcelAssertSettings(ExcelAssertSettingsBuilder builder) {
        caseSensitive = builder.caseSensitive;
        compareCellStyles = builder.compareCellStyles;
        sheetNames = builder.sheetNames;
        differencesStopCount = builder.differencesStopCount;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public boolean isCompareCellStyles() {
        return compareCellStyles;
    }

    public List<String> getSheetNames() {
        return sheetNames;
    }

    public int getDifferencesStopCount() {
        return differencesStopCount;
    }

    public static ExcelAssertSettingsBuilder builder() {
        return new ExcelAssertSettingsBuilder();
    }

    public static final class ExcelAssertSettingsBuilder {

        private boolean caseSensitive;
        private boolean compareCellStyles = true;
        private List<String> sheetNames = Collections.emptyList();
        private int differencesStopCount = 10;

        public ExcelAssertSettingsBuilder caseSensitive(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
            return this;
        }

        public ExcelAssertSettingsBuilder compareCellStyles(boolean compareCellStyles) {
            this.compareCellStyles = compareCellStyles;
            return this;
        }

        public ExcelAssertSettingsBuilder sheetName(String sheetName) {
            return sheetNames(List.of(sheetName));
        }

        public ExcelAssertSettingsBuilder sheetNames(List<String> sheetNames) {
            this.sheetNames = sheetNames;
            return this;
        }

        public ExcelAssertSettingsBuilder differencesStopCount(int differencesStopCount) {
            this.differencesStopCount = differencesStopCount;
            return this;
        }

        public ExcelAssertSettings build() {
            return new ExcelAssertSettings(this);
        }
    }
}
