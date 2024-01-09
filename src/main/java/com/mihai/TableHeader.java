package com.mihai;

import com.mihai.workbook.sheet.PropertyCell;

import java.util.*;

public class TableHeader {

    private final PropertyCell cell;
    private final List<TableHeader> children = new ArrayList<>();

    private TableHeader parent;

    public TableHeader(PropertyCell cell) {
        this.cell = cell;
    }

    public TableHeader copyWithoutParentReference() {
        TableHeader tableHeader = new TableHeader(cell);
        children.forEach(child -> {
            child = child.copyWithoutParentReference();
            tableHeader.addChildHeader(child);
            child.setParent(tableHeader);
        });
        return tableHeader;
    }

    public void addChildHeader(TableHeader child) {
        this.children.add(child);
    }

    public void setParent(TableHeader parent) {
        this.parent = parent;
    }

    public String getValue() {
        return cell.getValue();
    }

    public PropertyCell getCell() {
        return cell;
    }

    public int getColumnNumber() {
        return cell.getColumnNumber();
    }

    public TableHeader getRoot() {
        if (parent == null) {
            return this;
        }
        return parent.getRoot();
    }

    public TableHeader getParent() {
        return parent;
    }

    public List<TableHeader> getChildren() {
        return List.copyOf(children);
    }

    public List<TableHeader> getLeaves() {
        List<TableHeader> leafNodes = new ArrayList<>();
        if (children.isEmpty()) {
            return List.of(this);
        }
        for (TableHeader child : children) {
            leafNodes.addAll(child.getLeaves());
        }

        return leafNodes;
    }
}
