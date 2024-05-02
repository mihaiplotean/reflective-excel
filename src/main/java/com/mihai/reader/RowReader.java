package com.mihai.reader;

import com.mihai.ReflectionUtilities;
import com.mihai.reader.field.mapping.HeaderMappedField;
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

    public ReadableSheetContext getSheetContext() {
        return sheetContext;
    }

    public <T> T readRow(ReadableRow row, Class<T> clazz) {
        T object = ReflectionUtilities.newObject(clazz);

        Iterator<ReadableCell> cellIterator = sheetContext.createCellIterator(row);
        while (cellIterator.hasNext()) {
            ReadableCell cell = cellIterator.next();

            HeaderMappedField field = columnFieldMapping.getField(cell.getColumnNumber());
            if (field != null) {
                field.storeCurrentValue(sheetContext.getReadingContext());
            }
        }
        writeFieldValues(object);

        return object;
    }

    private <T> void writeFieldValues(T object) {
        List<HeaderMappedField> fields = columnFieldMapping.getAllFields();
        fields.forEach(fieldWithValue -> fieldWithValue.writeTo(object));
        fields.forEach(HeaderMappedField::resetValue);
    }

//    public AnnotatedFieldValueReader createFieldValueReader(AnnotatedField field) {
//        return createFieldValueReader(field, columnFieldMapping);
//    }

//    public AnnotatedFieldValueReader createFieldValueReader(AnnotatedField field, ColumnFieldMapping columnFieldMapping) {
//        AnnotatedFieldType type = field.getType();
//        return switch (type) {
//            case FIXED -> new FixedColumnFieldValueReader((FixedColumnField2) field);
//            case DYNAMIC -> new DynamicColumnFieldValueReader((DynamicColumnField) field);
//            case GROUPED -> new GroupedColumnsFieldValueReader((GroupedColumnsField) field, columnFieldMapping);
//            default -> throw new IllegalArgumentException();
//        };
//    }
}
