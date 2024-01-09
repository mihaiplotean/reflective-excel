package com.mihai.field;

import com.mihai.ReadingContext;
import com.mihai.TableHeader;
import com.mihai.field.value.AnnotatedFieldValue;
import com.mihai.field.value.FixedColumnFieldValue;

import java.lang.reflect.Field;

public class FixedColumnField implements AnnotatedHeaderField {

    private final String columnName;
    private final Field field;

    public FixedColumnField(String columnName, Field field) {
        this.columnName = columnName;
        this.field = field;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldValue newFieldValue() {
        return new FixedColumnFieldValue(field);
    }

    @Override
    public boolean canMapTo(ReadingContext context, TableHeader header) {
        return header.getValue().equalsIgnoreCase(columnName);
    }
}
