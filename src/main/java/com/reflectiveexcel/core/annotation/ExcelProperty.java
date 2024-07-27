package com.reflectiveexcel.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Similar to {@link ExcelCellValue}, with the difference that the value's property name and cell reference must be specified.
 * When reading, if the property name at the specified cell does not match the one in the sheet,
 * a {@link com.reflectiveexcel.reader.exception.BadInputException} is thrown. This is useful if you want to ensure the correctness
 * of the supplied sheet.
 * <pre>
 * Example - Assume that in the sheet's top left corner we have:
 *     +----------------+---------------+
 *     | Document Name: | Test document |
 *     +----------------+---------------+
 * Then {@link #name()} will be "Document Name:", {@link #nameReference()} will be "A1", and {@link #valueReference()} will be "A2".
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelProperty {

    String name();

    String nameReference();

    String valueReference();
}
