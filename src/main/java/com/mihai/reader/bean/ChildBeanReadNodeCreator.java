package com.mihai.reader.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.mihai.core.field.FieldAnalyzer;
import com.mihai.core.utils.ReflectionUtilities;

public class ChildBeanReadNodeCreator {

    private final Class<?> type;

    public ChildBeanReadNodeCreator(Class<?> type) {
        this.type = type;
    }

    public List<ChildBeanReadNode> getChildFields() {
        FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(type);

        List<ChildBeanReadNode> childFields = new ArrayList<>();

        childFields.addAll(fieldAnalyzer.getFixedColumnFields().stream()
                                   .map(FixedBeanReadNode::new)
                                   .toList());

        childFields.addAll(fieldAnalyzer.getDynamicColumnFields().stream()
                                   .map(DynamicBeanReadNode::new)
                                   .toList());

        childFields.addAll(fieldAnalyzer.getCellGroupFields().stream()
                                   .map(GroupBeanReadNode::new)
                                   .toList());

        List<Field> orderedFields = ReflectionUtilities.getAllFields(type);
        childFields.sort(Comparator.comparingInt(o -> orderedFields.indexOf(o.getField())));

        return childFields;
    }
}
