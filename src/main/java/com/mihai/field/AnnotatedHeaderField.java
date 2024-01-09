package com.mihai.field;

import com.mihai.ReadingContext;
import com.mihai.TableHeader;

public interface AnnotatedHeaderField extends AnnotatedField {

    boolean canMapTo(ReadingContext context, TableHeader header);
}
