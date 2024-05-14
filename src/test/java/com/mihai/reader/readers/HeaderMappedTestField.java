package com.mihai.reader.readers;

import com.mihai.common.field.AnnotatedField;
import com.mihai.common.utils.ReflectionUtilities;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.mapping.HeaderMappedField;
import com.mihai.reader.table.TableHeader;

import java.lang.reflect.Field;

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
