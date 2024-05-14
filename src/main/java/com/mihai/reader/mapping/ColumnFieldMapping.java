package com.mihai.reader.mapping;

import com.mihai.reader.table.TableHeaders;

import java.util.List;

public interface ColumnFieldMapping {

    void create(TableHeaders headers);

    HeaderMappedField getField(int columnIndex);

    List<HeaderMappedField> getAllFields();
}
