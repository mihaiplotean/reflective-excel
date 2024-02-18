package com.mihai.writer.node;

import com.mihai.FieldAnalyzer;
import com.mihai.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AnnotatedFieldNodeCreator {

    private final Class<?> type;
    private final Object target;

    public AnnotatedFieldNodeCreator(Class<?> type, Object target) {
        this.type = type;
        this.target = target;
    }

    public List<AnnotatedFieldNodeInterface> getChildFields() {
        FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(type);

        List<AnnotatedFieldNodeInterface> childFields = new ArrayList<>();

        childFields.addAll(fieldAnalyzer.getFixedColumnFields().stream()
                .map(field -> new FixedFieldNode(field.getField(), field.getColumnName()))
                .toList());

        childFields.addAll(fieldAnalyzer.getDynamicColumnFields().stream()
                .map(dynamicColumnField -> new DynamicFieldNode(dynamicColumnField.getField(), target))
                .toList());

        childFields.addAll(fieldAnalyzer.getCellGroupFields().stream()
                .map(field -> new GroupedFieldNode(field.getField(), ReflectionUtilities.readField(field.getField(), target), field.getGroupName()))
                .toList());

        List<Field> orderedFields = ReflectionUtilities.getAllFields(type);
        childFields.sort(Comparator.comparingInt(o -> orderedFields.indexOf(o.getField())));

        return childFields;
    }
}
