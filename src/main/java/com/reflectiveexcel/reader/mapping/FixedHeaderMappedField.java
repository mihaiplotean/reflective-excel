package com.reflectiveexcel.reader.mapping;

import com.reflectiveexcel.core.field.AnnotatedField;
import com.reflectiveexcel.core.field.FixedColumnField;
import com.reflectiveexcel.core.utils.ReflectionUtilities;
import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.table.TableHeader;

public class FixedHeaderMappedField implements HeaderMappedField {

    private final FixedColumnField field;

    private Object fieldValue;

    public FixedHeaderMappedField(FixedColumnField field) {
        this.field = field;
    }

    @Override
    public AnnotatedField getField() {
        return field;
    }

    @Override
    public boolean canMapTo(ReadingContext context, TableHeader header) {
        return header.getValue().equalsIgnoreCase(field.getColumnName());
    }

    @Override
    public void storeCurrentValue(ReadingContext readingContext) {
        fieldValue = readingContext.getCurrentCellValue(field.getFieldType());
    }

    @Override
    public void writeTo(Object targetObject) {
        ReflectionUtilities.writeField(field.getField(), targetObject, fieldValue);
    }

    @Override
    public void resetValue() {
        fieldValue = null;
    }
}
