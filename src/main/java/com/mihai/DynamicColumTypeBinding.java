package com.mihai;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class DynamicColumTypeBinding {

    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(
            List.class,
            Map.class
    );

    private final Field field;
    private Object object;

    public DynamicColumTypeBinding(Field field) {
        this.field = field;
    }

    public boolean isSupported() {
        return SUPPORTED_TYPES.contains(field.getType());
    }

    public List<? extends Class<?>> getGenericArgumentTypes() {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        return Arrays.stream(genericType.getActualTypeArguments())
                .filter(type -> type instanceof Class)
                .map(type -> (Class<?>) type)
                .toList();
    }

    public Object initializeObject() {
        if (object != null) {
            return object;
        }
        Class<?> type = field.getType();
        if (type == List.class) {
            return new ArrayList<>();
        }
        if (type == Map.class) {
            return new LinkedHashMap<>();
        }
        throw new IllegalStateException("Unknown type %s".formatted(type.getSimpleName()));
    }

    public void addToObject(Object... values) {
        if (object instanceof Map<?, ?>) {
            Map<? super Object, ? super Object> map = (Map<? super Object, ? super Object>) object;
            map.put(values[0], values[1]);
        }
        if(object instanceof List<?>) {
            List<? super Object> list = (List<? super Object>) object;
            list.add(values[0]);
        }
    }
}
