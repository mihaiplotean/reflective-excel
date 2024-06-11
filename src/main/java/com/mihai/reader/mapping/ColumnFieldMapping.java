package com.mihai.reader.mapping;

import java.util.List;

import com.mihai.reader.table.TableHeaders;

/**
 * Determines which cells of the table header map to which fields of the row object.
 */
public interface ColumnFieldMapping {

    void create(TableHeaders headers);

    HeaderMappedField getField(int columnIndex);

    List<HeaderMappedField> getAllFields();
}
