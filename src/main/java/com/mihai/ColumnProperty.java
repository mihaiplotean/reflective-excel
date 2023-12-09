package com.mihai;

import java.lang.reflect.Field;

public class ColumnProperty {

    private enum ColumnType {
        FIXED,
        DYNAMIC
    }

    private final ExcelCell cell;
    private final Field field;
    private final ColumnType columnType;

    private ColumnProperty(ExcelCell cell, Field field, ColumnType columnType) {
        this.cell = cell;
        this.field = field;
        this.columnType = columnType;
    }

    public static ColumnProperty fixedColumn(ExcelCell cell, Field field) {
        return new ColumnProperty(cell, field, ColumnType.FIXED);
    }

    public static ColumnProperty dynamicColumn(ExcelCell cell, Field field) {
        return new ColumnProperty(cell, field, ColumnType.DYNAMIC);
    }

    public String getColumnName() {
        return cell.getColumnName();
    }


    public ExcelCell getCell() {
        return cell;
    }

    public Field getField() {
        return field;
    }

    public Class<?> getFieldType() {
        return field.getType();
    }

    public boolean isDynamic() {
        return columnType == ColumnType.DYNAMIC;
    }

    public boolean isFixed() {
        return columnType == ColumnType.FIXED;
    }
}
