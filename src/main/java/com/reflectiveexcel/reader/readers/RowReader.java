package com.reflectiveexcel.reader.readers;

import java.util.Iterator;
import java.util.List;

import com.reflectiveexcel.core.utils.ReflectionUtilities;
import com.reflectiveexcel.reader.ReadableSheetContext;
import com.reflectiveexcel.reader.mapping.ColumnFieldMapping;
import com.reflectiveexcel.reader.mapping.HeaderMappedField;
import com.reflectiveexcel.reader.workbook.sheet.ReadableCell;
import com.reflectiveexcel.reader.workbook.sheet.ReadableRow;

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
