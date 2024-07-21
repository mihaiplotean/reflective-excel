package com.mihai.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated field should be mapped to a fixed column. A fixed column should always
 * appear in the Excel table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {

    /**
     * @return the name of the column/table header.
     */
    String value();
}
