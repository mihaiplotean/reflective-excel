package com.mihai.writer.node;

import com.mihai.FieldAnalyzer;
import com.mihai.ReflectionUtilities;
import com.mihai.field.AnnotatedHeaderField;
import com.mihai.field.DynamicColumnField;
import com.mihai.field.FixedColumnField;
import com.mihai.field.GroupedColumnsField;

import java.lang.reflect.Field;
import java.util.*;

public class AnnotatedFieldNode {

    private final Class<?> type;
    private final Object value;  // todo: return Object?
    private final Object target;
    private final List<AnnotatedFieldNode> children;
    private final AnnotatedHeaderField headerField;

    public AnnotatedFieldNode(Class<?> type, Object target) {
        this(type, target, "", null);
    }

    public AnnotatedFieldNode(Class<?> type, Object target, Object value, AnnotatedHeaderField headerField) {
        this.type = type;
        this.target = target;
        this.value = value;
        this.headerField = headerField;
        this.children = getChildFields();
    }

    public void writeValue(Object row) {

    }

    public Object getValue() {
        return value;
    }

    private List<AnnotatedFieldNode> getChildFields() {
        if (type == null) {
            return Collections.emptyList();
        }
        Map<Field, List<AnnotatedFieldNode>> sortedFieldMap = new LinkedHashMap<>();
        for (Field field : ReflectionUtilities.getAllFields(type)) {
            sortedFieldMap.put(field, Collections.emptyList());
        }

        FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(type);

        List<AnnotatedFieldNode> childFields = new ArrayList<>();
        List<AnnotatedFieldNode> fixedColumnNodes = new ArrayList<>();
        for (FixedColumnField fixedColumnField : fieldAnalyzer.getFixedColumnFields()) {
            AnnotatedFieldNode annotatedFieldNode = new AnnotatedFieldNode(fixedColumnField.getField().getType(), null, fixedColumnField.getColumnName(), fixedColumnField);
            fixedColumnNodes.add(annotatedFieldNode);
            sortedFieldMap.put(fixedColumnField.getField(), Collections.singletonList(annotatedFieldNode));
        }
        childFields.addAll(fixedColumnNodes);

        List<DynamicColumnField> dynamicColumnFields = fieldAnalyzer.getDynamicColumnFields();
        List<AnnotatedFieldNode> dynamicColumnNodes = new ArrayList<>();
        for (DynamicColumnField dynamicColumnField : dynamicColumnFields) {
            List<AnnotatedFieldNode> dynamicFieldChildren = getChildren(dynamicColumnField);
            dynamicColumnNodes.addAll(dynamicFieldChildren);
            sortedFieldMap.put(dynamicColumnField.getField(), dynamicFieldChildren);
        }
        childFields.addAll(dynamicColumnNodes);

        if (target != null) {
            List<AnnotatedFieldNode> cellGroupFields = new ArrayList<>();
            for (GroupedColumnsField field : fieldAnalyzer.getCellGroupFields()) {
                AnnotatedFieldNode annotatedFieldNode = new AnnotatedFieldNode(
                        field.getField().getType(),
                        ReflectionUtilities.readField(field.getField(), target),
                        field.getGroupName(),
                        field);
                cellGroupFields.add(annotatedFieldNode);
                sortedFieldMap.put(field.getField(), Collections.singletonList(annotatedFieldNode));
            }
            childFields.addAll(cellGroupFields);
        }

        return sortedFieldMap.values().stream()
                .flatMap(Collection::stream)
                .toList();
    }

    public int getLength() {
        if (children.isEmpty()) {
            return 1;
        }
        return children.stream()
                .mapToInt(AnnotatedFieldNode::getLength)
                .sum();
    }

    public int getHeight() {
        if (children.isEmpty()) {
            return 1;
        }
        return 1 + children.stream()
                .mapToInt(AnnotatedFieldNode::getHeight)
                .max()
                .getAsInt();
    }

    public List<AnnotatedFieldNode> getChildren() {
        return children;
    }

    private List<AnnotatedFieldNode> getChildren(DynamicColumnField field) {
        if (target == null) {
            return Collections.emptyList();
        }
        Field actualField = field.getField();
        Class<?> fieldType = actualField.getType();
        if (fieldType == Map.class) {
            Map<Object, Object> map = (Map<Object, Object>) ReflectionUtilities.readField(actualField, target);
            return map.keySet().stream()
                    .map(name -> new AnnotatedFieldNode(null, null, name, field))
                    .toList();
        }
        assert false : "type not allowed";
        return Collections.emptyList();
    }

    public List<AnnotatedFieldNode> getLeaves() {
        if(children.isEmpty()) {
            return Collections.singletonList(this);
        }
        return children.stream()
                .flatMap(child -> child.getLeaves().stream())
                .toList();
    }
}
