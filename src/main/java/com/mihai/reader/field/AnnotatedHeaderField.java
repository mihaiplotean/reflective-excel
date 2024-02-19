package com.mihai.reader.field;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.TableHeader;

public interface AnnotatedHeaderField extends AnnotatedField {

    boolean canMapTo(ReadingContext context, TableHeader header);
}
