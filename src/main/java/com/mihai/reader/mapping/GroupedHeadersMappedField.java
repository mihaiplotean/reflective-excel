package com.mihai.reader.mapping;

import com.mihai.core.utils.ReflectionUtilities;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.table.TableHeader;
import com.mihai.reader.table.TableHeaders;
import com.mihai.reader.bean.ChildBeanReadNode;
import com.mihai.core.field.GroupedColumnsField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GroupedHeadersMappedField implements HeaderMappedField {

    private final GroupedColumnsField field;
    private final List<HeaderMappedField> groupFieldValues = new ArrayList<>();

    private ColumnFieldMapping columnFieldMapping;

    public GroupedHeadersMappedField(GroupedColumnsField field) {
        this.field = field;
    }

    @Override
    public GroupedColumnsField getField() {
        return field;
    }

    @Override
    public boolean canMapTo(ReadingContext context, TableHeader header) {
        ChildBeanReadNode beanNode = getAllBeans(context.getCurrentTableBean().getChildren()).stream()
                .filter(node -> field.getField().equals(node.getField()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Field to be mapped not present in bean!"));
        TableHeader headerRoot = header.getRoot();
        boolean fieldCanBeMappedToHeader = headerNamesMatchNodeNames(headerRoot, beanNode);
        if (fieldCanBeMappedToHeader) {
            columnFieldMapping = new DefaultColumnFieldMapping(context, getField().getFieldType());
            columnFieldMapping.create(subHeaders(headerRoot));
        }
        return fieldCanBeMappedToHeader;
    }

    private static List<ChildBeanReadNode> getAllBeans(List<ChildBeanReadNode> nodes) {
        List<ChildBeanReadNode> allNodes = new ArrayList<>(nodes);
        for (ChildBeanReadNode node : nodes) {
            allNodes.addAll(getAllBeans(node.getChildren()));
        }
        return allNodes;
    }

    private boolean headerNamesMatchNodeNames(TableHeader header, ChildBeanReadNode childBeanNode) {
        if (childBeanNode.getName() == null) {  // can happen for dynamic headers
            return true;  // we do not know the dynamic header name, so we assume it could be mapped
        }
        if (!header.getValue().equalsIgnoreCase(childBeanNode.getName())) {
            return false;
        }
        for (ChildBeanReadNode childNode : childBeanNode.getChildren()) {
            return header.getChildren().stream()
                    .anyMatch(childHeader -> headerNamesMatchNodeNames(childHeader, childNode));
        }
        return true;
    }

    private TableHeaders subHeaders(TableHeader header) {
        List<TableHeader> subHeaders = new ArrayList<>();
        for (TableHeader subHeader : header.getChildren()) {
            subHeaders.addAll(subHeader.copyWithoutParentReference().getLeaves());
        }
        return new TableHeaders(subHeaders);
    }

    @Override
    public void storeCurrentValue(ReadingContext readingContext) {
        HeaderMappedField field = columnFieldMapping.getField(readingContext.getCurrentCell().getColumnNumber());
        if (field != null) {
            groupFieldValues.add(field);
            field.storeCurrentValue(readingContext);
        }
    }

    @Override
    public void writeTo(Object targetObject) {
        Field field = this.field.getField();
        Object cellGroupObject = ReflectionUtilities.newObject(field.getType());
        groupFieldValues.forEach(value -> value.writeTo(cellGroupObject));
        ReflectionUtilities.writeField(field, targetObject, cellGroupObject);
    }

    @Override
    public void resetValue() {
        groupFieldValues.clear();
    }
}
