package com.mihai.core.annotation;

import com.mihai.reader.detector.AlwaysTrueColumnDetector;
import com.mihai.reader.detector.ColumnDetector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DynamicColumns {

    Class<? extends ColumnDetector> detector() default AlwaysTrueColumnDetector.class;
}
