package com.reflectiveexcel.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applies filtering to all the columns of the annotated table type.
 * In Excel, this corresponds to "Data Tab" > "Filter".
 * Note that it can be applied to only one table in a sheet.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InstallFilter {

}
