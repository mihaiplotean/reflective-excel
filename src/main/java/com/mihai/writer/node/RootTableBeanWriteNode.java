package com.mihai.writer.node;

import java.util.List;

public class RootTableBeanWriteNode {

    private final Class<?> type;
    private final Object target;
    private final List<ChildBeanWriteNode> children;

    public RootTableBeanWriteNode(Class<?> type, Object target) {
        this.type = type;
        this.target = target;
        this.children = getChildFields();
    }

    private List<ChildBeanWriteNode> getChildFields() {
        return new ChildBeanWriteNodeCreator(type, target).getChildFields();
    }

    public int getLength() {
        return children.stream()
                .mapToInt(ChildBeanWriteNode::getLength)
                .sum();
    }

    public int getHeight() {
        int height = children.stream()
                .mapToInt(ChildBeanWriteNode::getHeight)
                .max()
                .orElse(0);
        return height == 0 ? 1 : height;
    }

    public List<ChildBeanWriteNode> getChildren() {
        return children;
    }
}
