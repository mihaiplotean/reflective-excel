package com.mihai.core.field;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mihai.core.utils.ReflectionUtilities;
import com.mihai.reader.detector.ColumnDetector;

public class DynamicColumnField implements AnnotatedField {

    private static final Set<Class<?>> SUPPORTED_DYNAMIC_FIELD_TYPES = Set.of(
            List.class,
            Map.class
    );

    private final ColumnDetector columnDetector;
    private final Field field;

    public DynamicColumnField(Field field, ColumnDetector columnDetector) {
        validateFieldType(field);
        this.columnDetector = columnDetector;
        this.field = field;
    }

    private static void validateFieldType(Field field) {
        Class<?> type = field.getType();
        if (!isSupportedDynamicField(type)) {
            throw new IllegalStateException(String.format(
                    "Unsupported type %s annotated as dynamic column. Only <%s> can be annotated.", type,
                    SUPPORTED_DYNAMIC_FIELD_TYPES.stream()
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", "))
            ));
        }
        if (!hasValidTypeParameters(field)) {
            throw new IllegalStateException("Generic type parameters cannot be a generic type!");
        }
    }

    private static boolean isSupportedDynamicField(Class<?> clazz) {
        return SUPPORTED_DYNAMIC_FIELD_TYPES.stream()
                .anyMatch(clazz::equals);
    }

    private static boolean hasValidTypeParameters(Field field) {
        return ReflectionUtilities.hasClassTypeParameters(field);
    }

    public ColumnDetector getColumnDetector() {
        return columnDetector;
    }

    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldType getType() {
        return AnnotatedFieldType.DYNAMIC;
    }
}
