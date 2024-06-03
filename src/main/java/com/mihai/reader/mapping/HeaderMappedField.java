package com.mihai.reader.mapping;

import com.mihai.core.field.AnnotatedField;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.table.TableHeader;

public interface HeaderMappedField {

    AnnotatedField getField();

    boolean canMapTo(ReadingContext context, TableHeader header);

    void storeCurrentValue(ReadingContext readingContext);

    void writeTo(Object targetObject);

    void resetValue();
}
