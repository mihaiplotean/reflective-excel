package com.mihai.writer.node;

import java.util.List;

public class RootFieldNode {

    private final Class<?> type;
    private final Object target;
    private final List<AnnotatedFieldNode> children;

    public RootFieldNode(Class<?> type, Object target) {
        this.type = type;
        this.target = target;
        this.children = getChildFields();
    }

    private List<AnnotatedFieldNode> getChildFields() {
//        FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(type);
//
//        List<AnnotatedFieldNodeInterface> childFields = new ArrayList<>();
//
//        childFields.addAll(fieldAnalyzer.getFixedColumnFields().stream()
//                .map(field -> new FixedFieldNode(field.getField(), field.getColumnName()))
//                .toList());
//
//        childFields.addAll(fieldAnalyzer.getDynamicColumnFields().stream()
//                .map(dynamicColumnField -> new DynamicFieldNode(dynamicColumnField.getField(), target))
//                .toList());
//
//        childFields.addAll(fieldAnalyzer.getCellGroupFields().stream()
//                .map(field -> new GroupedFieldNode(field.getField(), ReflectionUtilities.readField(field.getField(), target), field.getGroupName()))
//                .toList());
//
//        List<Field> orderedFields = ReflectionUtilities.getAllFields(type);
//        childFields.sort(Comparator.comparingInt(o -> orderedFields.indexOf(o.getField())));

        return new AnnotatedFieldNodeCreator(type, target).getChildFields();
    }

    public int getLength() {
        return children.stream()
                .mapToInt(AnnotatedFieldNode::getLength)
                .sum();
    }

    public int getHeight() {
        return children.stream()
                .mapToInt(AnnotatedFieldNode::getHeight)
                .max()
                .orElse(0);
    }

    public List<AnnotatedFieldNode> getChildren() {
        return children;
    }
}
