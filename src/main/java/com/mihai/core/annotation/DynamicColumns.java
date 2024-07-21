package com.mihai.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mihai.reader.detector.AlwaysTrueDynamicColumnDetector;
import com.mihai.reader.detector.DynamicColumnDetector;

/**
 * Indicates that the annotated field should be mapped to dynamic columns in the Excel sheet. Dynamic columns are columns
 * which do not always appear in a sheet.
 * <p>
 * In case of reading, the annotated field can either be a map or a list. In a map, the keys will represent the column names.
 * The values in the map, as well as the list, will represent the row value corresponding to the column. The map keys
 * and the map/list values are deserialized using the specified deserializer for the generic parameter type(s).
 * Checking if a column should be mapped to the annotated field is done through the {@link #detector()}.
 * Let's say that the table you want to read contains a month's days as columns. In this case, the {@link #detector()} should
 * return true if the cell value is between 1 and 31.
 * </p>
 * <p>
 * In case of writing, the annotated field can only be a map. The map keys will be written to the sheet as column names. The map keys
 * and values are serialized using the specified serializer for the generic parameter type(s). Note that the excel writer
 * creates the dynamic header columns based on the first provided row, so it is your responsibility to make sure that the first row
 * has the map keys corresponding to the dynamic columns that you would like to show.
 * The {@link #detector()} serves no purpose in case of writing, so it can be ignored.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DynamicColumns {

    /**
     * Used to specify which columns are dynamic when reading. By default, all columns which could not be mapped as fixed
     * or grouped columns are considered dynamic.
     */
    Class<? extends DynamicColumnDetector> detector() default AlwaysTrueDynamicColumnDetector.class;
}
