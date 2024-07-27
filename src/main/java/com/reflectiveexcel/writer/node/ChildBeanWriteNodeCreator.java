package com.reflectiveexcel.writer.node;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.reflectiveexcel.core.field.FieldAnalyzer;
import com.reflectiveexcel.core.utils.ReflectionUtilities;

public class ChildBeanWriteNodeCreator {

    private final Class<?> type;
    private final Object target;

    public ChildBeanWriteNodeCreator(Class<?> type, Object target) {
        this.type = type;
        this.target = target;
    }

    public List<ChildBeanWriteNode> getChildFields() {
        FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(type);

        List<ChildBeanWriteNode> childFields = new ArrayList<>();

        childFields.addAll(fieldAnalyzer.getFixedColumnFields().stream()
                                   .map(field -> new FixedBeanWriteNode(field.getField(), field.getColumnName()))
                                   .toList());

        childFields.addAll(fieldAnalyzer.getDynamicColumnFields().stream()
                                   .map(dynamicColumnField -> new DynamicBeanWriteNode(dynamicColumnField.getField(), target))
                                   .toList());

        childFields.addAll(fieldAnalyzer.getCellGroupFields().stream()
                                   .map(field -> new GroupBeanWriteNode(field.getField(),
                                                                        ReflectionUtilities.readField(field.getField(), target),
                                                                        field.getGroupName()))
                                   .toList());

        List<Field> orderedFields = ReflectionUtilities.getAllFields(type);
        childFields.sort(Comparator.comparingInt(o -> orderedFields.indexOf(o.getField())));

        return childFields;
    }
}
