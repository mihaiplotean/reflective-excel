package com.mihai.reader.bean;

import com.mihai.FieldAnalyzer;
import com.mihai.ReflectionUtilities;
import com.mihai.writer.node.AnnotatedFieldNode;
import com.mihai.writer.node.DynamicFieldNode;
import com.mihai.writer.node.FixedFieldNode;
import com.mihai.writer.node.GroupedFieldNode;

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
                .map(field -> new GroupBeanNode(field))
                .toList());

        List<Field> orderedFields = ReflectionUtilities.getAllFields(type);
        childFields.sort(Comparator.comparingInt(o -> orderedFields.indexOf(o.getField())));

        return childFields;
    }
}
