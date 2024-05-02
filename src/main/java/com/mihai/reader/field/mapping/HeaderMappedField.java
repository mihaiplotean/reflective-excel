package com.mihai.reader.field.mapping;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.TableHeader;
import com.mihai.reader.field.AnnotatedField;

public interface HeaderMappedField {

    AnnotatedField getField();

    boolean canMapTo(ReadingContext context, TableHeader header);

    void storeCurrentValue(ReadingContext readingContext);

    void writeTo(Object targetObject);

    void resetValue();
}
