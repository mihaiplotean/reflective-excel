package com.mihai.annotation;


import com.mihai.ExcelPropertyValueLocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelProperty {

    String name();

    String cellReference();

    ExcelPropertyValueLocation valueLocation() default ExcelPropertyValueLocation.ON_THE_LEFT;
}
