package com.mihai.reader.field.mapping;

import com.mihai.ReflectionUtilities;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.TableHeader;
import com.mihai.reader.field.AnnotatedField;
import com.mihai.reader.field.FixedColumnField;


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
