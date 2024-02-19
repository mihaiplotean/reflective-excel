package com.mihai.annotation;

import com.mihai.reader.detector.ColumnDetector;
import com.mihai.reader.detector.RowDetector;
import com.mihai.reader.detector.UseAsSpecifiedInReadSettings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelRows {

    Class<? extends RowDetector> lastRowDetector() default UseAsSpecifiedInReadSettings.class;

    Class<? extends RowDetector> skipRowDetector() default UseAsSpecifiedInReadSettings.class;

    Class<? extends RowDetector> headerRowDetector() default UseAsSpecifiedInReadSettings.class;

    Class<? extends ColumnDetector> headerStartColumnDetector() default UseAsSpecifiedInReadSettings.class;

    Class<? extends ColumnDetector> headerEndColumnDetector() default UseAsSpecifiedInReadSettings.class;
}
