package com.mihai.reader.mapping;

import java.util.List;

import com.mihai.reader.table.TableHeaders;

public interface ColumnFieldMapping {

    void create(TableHeaders headers);

    HeaderMappedField getField(int columnIndex);

    List<HeaderMappedField> getAllFields();
}
