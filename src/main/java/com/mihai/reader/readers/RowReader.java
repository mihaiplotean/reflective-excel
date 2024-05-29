package com.mihai.reader.readers;

import com.mihai.core.utils.ReflectionUtilities;
import com.mihai.reader.mapping.ColumnFieldMapping;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.mapping.HeaderMappedField;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;

import java.util.*;

public class RowReader {

    private final ReadableSheetContext sheetContext;
    private final ColumnFieldMapping columnFieldMapping;

    public RowReader(ReadableSheetContext sheetContext, ColumnFieldMapping columnFieldMapping) {
        this.sheetContext = sheetContext;
        this.columnFieldMapping = columnFieldMapping;
    }

    public <T> T readRow(ReadableRow row, Class<T> clazz) {
        T object = ReflectionUtilities.newObject(clazz);

        readFieldValues(row);
        writeFieldValues(object);

        return object;
    }

    private void readFieldValues(ReadableRow row) {
        Iterator<ReadableCell> cellIterator = sheetContext.createCellIterator(row);
        while (cellIterator.hasNext()) {
            ReadableCell cell = cellIterator.next();

            HeaderMappedField field = columnFieldMapping.getField(cell.getColumnNumber());
            if (field != null) {
                field.storeCurrentValue(sheetContext.getReadingContext());
            }
        }
    }

    private <T> void writeFieldValues(T object) {
        List<HeaderMappedField> fields = columnFieldMapping.getAllFields();
        fields.forEach(fieldWithValue -> fieldWithValue.writeTo(object));
        fields.forEach(HeaderMappedField::resetValue);
    }
}
