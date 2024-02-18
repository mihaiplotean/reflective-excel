package com.mihai.field;

import com.mihai.ColumnFieldMapping;
import com.mihai.ReadingContext;
import com.mihai.TableHeader;
import com.mihai.TableHeaders;
import com.mihai.field.value.AnnotatedFieldValue;
import com.mihai.field.value.GroupedColumnsFieldValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GroupedColumnsField implements AnnotatedHeaderField {

    private final String groupName;
    private final Field field;

    private ColumnFieldMapping columnFieldMapping;

    public GroupedColumnsField(String groupName, Field field) {
        this.groupName = groupName;
        this.field = field;
    }

    @Override
    public Field getField() {
        return field;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public AnnotatedFieldValue newFieldValue() {
        return new GroupedColumnsFieldValue(field, columnFieldMapping);
    }

    @Override
    public boolean canMapTo(ReadingContext context, TableHeader header) {
        boolean canMap = header.getValue().equalsIgnoreCase(groupName);
        if(canMap) {
            columnFieldMapping = new ColumnFieldMapping(context, field.getType());
            columnFieldMapping.create(subHeaders(header));
        }
        return canMap;
    }

    private TableHeaders subHeaders(TableHeader header) {
        List<TableHeader> subHeaders = new ArrayList<>();
        for (TableHeader subHeader : header.getChildren()) {
            subHeaders.addAll(subHeader.copyWithoutParentReference().getLeaves());
        }
        return new TableHeaders(subHeaders);
    }
}
