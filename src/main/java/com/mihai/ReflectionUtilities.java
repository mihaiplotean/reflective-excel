package com.mihai;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.reflect.FieldUtils.getAllFieldsList;

public class ReflectionUtilities {

    private ReflectionUtilities() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T newObject(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalStateException("Could not create new class instance", e);
        }
    }

    public static void writeField(Field field, Object targetObject, Object fieldValue) {
        try {
            FieldUtils.writeField(field, targetObject, fieldValue, true);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field must be non-final", e);
        }
    }

    public static Object readField(Field field, Object targetObject) {
        try {
            return FieldUtils.readField(field, targetObject, true);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field must be non-final", e);
        }
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }
        fields.addAll(List.of(clazz.getDeclaredFields()));
        return fields;
    }
}
