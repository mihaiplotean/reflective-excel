package com.mihai.reader.bean;

import com.mihai.common.annotation.TableId;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RootTableBeanNode {

    private final Class<?> type;
    private final String tableId;
    private final List<ChildBeanNode> children;

    public RootTableBeanNode(Class<?> type) {
        this.type = type;
        TableId tableIdAnnotation = type.getAnnotation(TableId.class);
        this.tableId = tableIdAnnotation == null ? null : tableIdAnnotation.value();
        this.children = getChildFields();
    }

    public RootTableBeanNode(Class<?> type, String tableId) {
        this.type = type;
        this.tableId = tableId;
        this.children = getChildFields();
    }

    public String getTableId() {
        return tableId;
    }

    private List<ChildBeanNode> getChildFields() {
        return new ChildBeanNodeCreator(type).getChildFields();
    }

    public List<ChildBeanNode> getChildren() {
        return children;
    }

    public List<ChildBeanNode> getLeaves() {
        return children.stream()
                .map(ChildBeanNode::getLeaves)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
