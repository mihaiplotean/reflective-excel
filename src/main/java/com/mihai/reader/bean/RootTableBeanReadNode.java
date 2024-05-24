package com.mihai.reader.bean;

import com.mihai.common.annotation.TableId;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RootTableBeanReadNode {

    private final Class<?> type;
    private final String tableId;
    private final List<ChildBeanReadNode> children;

    public RootTableBeanReadNode(Class<?> type) {
        this.type = type;
        TableId tableIdAnnotation = type.getAnnotation(TableId.class);
        this.tableId = tableIdAnnotation == null ? null : tableIdAnnotation.value();
        this.children = getChildFields();
    }

    public RootTableBeanReadNode(Class<?> type, String tableId) {
        this.type = type;
        this.tableId = tableId;
        this.children = getChildFields();
    }

    public String getTableId() {
        return tableId;
    }

    private List<ChildBeanReadNode> getChildFields() {
        return new ChildBeanReadNodeCreator(type).getChildFields();
    }

    public List<ChildBeanReadNode> getChildren() {
        return children;
    }

    public List<ChildBeanReadNode> getLeaves() {
        return children.stream()
                .map(ChildBeanReadNode::getLeaves)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
