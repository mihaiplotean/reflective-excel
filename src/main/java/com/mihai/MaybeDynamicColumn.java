package com.mihai;

public class MaybeDynamicColumn {

    private final ReadingContext context;
    private final PropertyCell column;

    public MaybeDynamicColumn(ReadingContext context, PropertyCell column) {
        this.context = context;
        this.column = column;
    }

    public String getName() {
        return column.getValue();
    }

    public int getIndex() {
        return column.getColumnNumber();
    }

    public boolean isAfter(String otherColumn) {
        int otherColumnIndex = indexOf(otherColumn);
        if(otherColumnIndex < 0) {
            return false;
        }
        return getIndex() > otherColumnIndex;
    }

    public boolean isBefore(String otherColumn) {
        int otherColumnIndex = indexOf(otherColumn);
        if(otherColumnIndex < 0) {
            return false;
        }
        return getIndex() < indexOf(otherColumn);
    }

    public boolean isBetween(String columnA, String columnB) {
        return isAfter(columnA) && isBefore(columnB);
    }

    public boolean equals(String other) {
        return getName().equalsIgnoreCase(other);
    }

    public boolean matchesPattern(String pattern) {
        return getName().matches(pattern);
    }

    private int indexOf(String columnName) {
        assert context.getCurrentRowNumber() == column.getRowNumber() : "Current column must be on the same row as the currently processed row";

        RowCells currentRow = context.getCurrentRow();
        for (PropertyCell cell : currentRow) {
            if(columnName.equalsIgnoreCase(cell.getValue())) {
                return cell.getColumnNumber();
            }
        }
        return -1;
    }
}
