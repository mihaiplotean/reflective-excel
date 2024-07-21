package com.mihai.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Should be used if you would like to map the value from the given cell to the annotated field in case of reading,
 * or vice-verse, in case of writing.
 * The field(s) will be mapped when reading using {@link com.mihai.reader.ReflectiveExcelReader#read(Class)} or,
 * when writing - using {@link com.mihai.writer.ReflectiveExcelWriter#write(Object)}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelCellValue {

    /**
     * @return cell reference of the cell whose value should be read or written. Example: "A1", "B2".
     */
    String cellReference();
}
