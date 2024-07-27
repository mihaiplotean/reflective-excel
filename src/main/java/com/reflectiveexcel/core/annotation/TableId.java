package com.reflectiveexcel.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to specify the id/name of the table that is being written or read. When used on fields, only the List type can be annotated.
 * You can use this annotation to read/write multiple tables at once from/to one sheet.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface TableId {

    String value();
}
