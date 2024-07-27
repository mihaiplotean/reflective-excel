package com.reflectiveexcel.reader.mapping;

import com.reflectiveexcel.core.field.AnnotatedField;
import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.table.TableHeader;

public interface HeaderMappedField {

    AnnotatedField getField();

    boolean canMapTo(ReadingContext context, TableHeader header);

    void storeCurrentValue(ReadingContext readingContext);

    void writeTo(Object targetObject);

    void resetValue();
}
