package com.mihai.reader.readers;

import com.mihai.reader.mapping.ColumnFieldMapping;
import com.mihai.reader.mapping.HeaderMappedField;
import com.mihai.reader.table.TableHeaders;

import java.util.List;
import java.util.Map;

public class ColumnFieldTestMapping implements ColumnFieldMapping {

    private final Map<Integer, HeaderMappedField> columnToFieldMap;

    public ColumnFieldTestMapping(Map<Integer, HeaderMappedField> columnToFieldMap) {
        this.columnToFieldMap = columnToFieldMap;
    }

    @Override
    public void create(TableHeaders headers) {
        // do nothing
    }

    @Override
    public HeaderMappedField getField(int columnIndex) {
        return columnToFieldMap.get(columnIndex);
    }

    @Override
    public List<HeaderMappedField> getAllFields() {
        return List.copyOf(columnToFieldMap.values());
    }
}
