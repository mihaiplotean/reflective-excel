package com.reflectiveexcel.reader.readers;

import java.lang.reflect.Field;

import com.reflectiveexcel.core.field.AnnotatedField;
import com.reflectiveexcel.core.utils.ReflectionUtilities;
import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.mapping.HeaderMappedField;
import com.reflectiveexcel.reader.table.TableHeader;

public class HeaderMappedTestField implements HeaderMappedField {

    private final Field field;

    private Object value;

    public HeaderMappedTestField(Field field) {
        this.field = field;
    }

    @Override
    public AnnotatedField getField() {
        return null;
    }

    @Override
    public boolean canMapTo(ReadingContext context, TableHeader header) {
        return true;
    }

    @Override
    public void storeCurrentValue(ReadingContext readingContext) {
        this.value = readingContext.getCurrentCellValue(field.getType());
    }

    @Override
    public void writeTo(Object targetObject) {
        ReflectionUtilities.writeField(field, targetObject, value);
    }

    @Override
    public void resetValue() {
        // do nothing
    }
}
