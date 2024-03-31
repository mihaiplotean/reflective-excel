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
        return new AnnotatedFieldNodeCreator(type, target).getChildFields();
    }

    public int getLength() {
        return children.stream()
                .mapToInt(AnnotatedFieldNode::getLength)
                .sum();
    }

    public int getHeight() {
        int height = children.stream()
                .mapToInt(AnnotatedFieldNode::getHeight)
                .max()
                .orElse(0);
        return height == 0 ? 1 : height;
    }

    public List<AnnotatedFieldNode> getChildren() {
        return children;
    }
}
