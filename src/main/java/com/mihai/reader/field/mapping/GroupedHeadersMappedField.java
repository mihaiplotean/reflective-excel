package com.mihai.reader.field.mapping;

import com.mihai.ReflectionUtilities;
import com.mihai.reader.ColumnFieldMapping;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.TableHeader;
import com.mihai.reader.TableHeaders;
import com.mihai.reader.bean.ChildBeanNode;
import com.mihai.reader.field.GroupedColumnsField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GroupedHeadersMappedField implements HeaderMappedField {

    private final GroupedColumnsField field;

    private ColumnFieldMapping columnFieldMapping;
    private List<HeaderMappedField> groupFieldValues;

    public GroupedHeadersMappedField(GroupedColumnsField field) {
        this.field = field;
    }

    @Override
    public GroupedColumnsField getField() {
        return field;
    }

    @Override
    public boolean canMapTo(ReadingContext context, TableHeader header) {
        ChildBeanNode beanNode = context.getCurrentTableBean().getChildren().stream()
                .filter(node -> node.getField() == field.getField())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Field to be mapped not present in bean!"));
        boolean fieldCanBeMappedToHeader = headerNamesMatchNodeNames(header.getRoot(), beanNode);
        if (fieldCanBeMappedToHeader) {
            columnFieldMapping = new ColumnFieldMapping(context, getField().getFieldType());
            columnFieldMapping.create(subHeaders(header));
        }
        return fieldCanBeMappedToHeader;
    }

    private boolean headerNamesMatchNodeNames(TableHeader header, ChildBeanNode childBeanNode) {
        if (childBeanNode.getName() == null) {  // can happen for dynamic headers
            return true;  // we do not know the dynamic header name, so we assume it could be mapped
        }
        if (!header.getValue().equalsIgnoreCase(childBeanNode.getName())) {
            return false;
        }
        for (ChildBeanNode childNode : childBeanNode.getChildren()) {
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
        groupFieldValues = null;
    }
}
