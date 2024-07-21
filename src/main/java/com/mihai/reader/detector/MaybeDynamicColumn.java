package com.mihai.reader.detector;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;

/**
 * Provides helpful methods to determine if a column is dynamic.
 */
public class MaybeDynamicColumn {

    private final ReadingContext context;
    private final ReadableCell cell;

    public MaybeDynamicColumn(ReadingContext context, ReadableCell cell) {
        this.context = context;
        this.cell = cell;
    }

    /**
     * Returns the name of the column.
     *
     * @return the name of the column.
     */
    public String getName() {
        return cell.getValue();
    }

    /**
     * Returns the column index of the column.
     *
     * @return the column index of the column.
     */
    public int getIndex() {
        return cell.getColumnNumber();
    }

    /**
     * Checks if this column is located after a column with the given name.
     *
     * @param otherColumn the name of the other column.
     * @return true if this column is located after a column with the given name.
     */
    public boolean isAfter(String otherColumn) {
        int otherColumnIndex = indexOf(otherColumn);
        if (otherColumnIndex < 0) {
            return false;
        }
        return getIndex() > otherColumnIndex;
    }

    /**
     * Checks if this column is located before a column with the given name.
     *
     * @param otherColumn the name of the other column.
     * @return true if this column is located before a column with the given name.
     */
    public boolean isBefore(String otherColumn) {
        int otherColumnIndex = indexOf(otherColumn);
        if (otherColumnIndex < 0) {
            return false;
        }
        return getIndex() < indexOf(otherColumn);
    }

    /**
     * Checks if this column is located between two column with the given names.
     *
     * @param columnA the name of the column that should be on the left.
     * @param columnB the name of the column that should be on the right.
     * @return true if this column is located between two column with the given names.
     */
    public boolean isBetween(String columnA, String columnB) {
        return isAfter(columnA) && isBefore(columnB);
    }

    /**
     * Checks if this column has the given name, case insensitively!
     * The check is case-insensitive as that is the default in the whole framework.
     *
     * @param other name of the column.
     * @return true if the column has the given name.
     */
    public boolean equals(String other) {
        return getName().equalsIgnoreCase(other);
    }

    /**
     * Checks if this column name matches the given pattern.
     *
     * @param pattern pattern to check the column name against.
     * @return true if this column name matches the given pattern.
     */
    public boolean matchesPattern(String pattern) {
        return getName().matches(pattern);
    }

    private int indexOf(String columnName) {
        ReadableRow currentRow = context.getCurrentRow();
        for (ReadableCell cell : currentRow) {
            if (columnName.equalsIgnoreCase(cell.getValue())) {
                return cell.getColumnNumber();
            }
        }
        return -1;
    }
}
