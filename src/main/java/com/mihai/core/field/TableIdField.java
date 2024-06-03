package com.mihai.core.field;

import java.lang.reflect.Field;
import java.util.List;

import com.mihai.core.utils.ReflectionUtilities;

public class TableIdField implements AnnotatedField {

    private final Field field;
    private final String tableId;

    public TableIdField(Field field, String tableId) {
        validateFieldType(field);
        this.field = field;
        this.tableId = tableId;
    }

    private static void validateFieldType(Field field) {
        Class<?> type = field.getType();
        if (type != List.class) {
            throw new IllegalStateException(String.format(
                    "Unsupported type %s annotated as ta column. Only List.class can be annotated.", type
            ));
        }
        if (!hasValidTypeParameters(field)) {
            throw new IllegalStateException("Generic type parameters cannot be a generic type!");
        }
    }

    private static boolean hasValidTypeParameters(Field field) {
        return ReflectionUtilities.hasClassTypeParameters(field);
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldType getType() {
        return AnnotatedFieldType.TABLE_ID;
    }

    public String getTableId() {
        return tableId;
    }
}
