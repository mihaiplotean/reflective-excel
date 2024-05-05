package com.mihai.reader.bean;

import com.mihai.common.field.FieldAnalyzer;
import com.mihai.common.utils.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChildBeanNodeCreator {

    private final Class<?> type;

    public ChildBeanNodeCreator(Class<?> type) {
        this.type = type;
    }

    public List<ChildBeanNode> getChildFields() {
        FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(type);

        List<ChildBeanNode> childFields = new ArrayList<>();

        childFields.addAll(fieldAnalyzer.getFixedColumnFields().stream()
                .map(FixedFieldBeanNode::new)
                .toList());

        childFields.addAll(fieldAnalyzer.getDynamicColumnFields().stream()
                .map(DynamicFieldBeanNode::new)
                .toList());

        childFields.addAll(fieldAnalyzer.getCellGroupFields().stream()
                .map(GroupBeanNode::new)
                .toList());

        List<Field> orderedFields = ReflectionUtilities.getAllFields(type);
        childFields.sort(Comparator.comparingInt(o -> orderedFields.indexOf(o.getField())));

        return childFields;
    }
}
