package com.mihai.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelRow {

    String sheetName() default "";

//    int headerStartRow() default 0;

//    int headerStartColumn() default 0;
}
