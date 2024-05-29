package com.mihai.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtilities {

    private ReflectionUtilities() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T newObject(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalStateException("Could not create new class instance. Make sure you have an empty public constructor.", e);
        }
    }

    public static void writeField(Field field, Object targetObject, Object fieldValue) {
        try {
            field.setAccessible(true);
            field.set(targetObject, fieldValue);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field \"%s\" must be non-final".formatted(field.toString()), e);
        }
    }

    public static Object readField(Field field, Object targetObject) {
        try {
            field.setAccessible(true);
            return field.get(targetObject);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field \"%s\" must be accessible".formatted(field.toString()), e);
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

    public static boolean hasClassTypeParameters(Field field) {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        return Arrays.stream(genericType.getActualTypeArguments())
                .allMatch(type -> type instanceof Class<?>);
    }
}
