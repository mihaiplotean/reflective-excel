package com.reflectiveexcel.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.reflectiveexcel.writer.node.ColumnSizePreferences;
import com.reflectiveexcel.core.annotation.ExcelColumn;
import com.reflectiveexcel.core.annotation.DynamicColumns;

/**
 * Annotation used to specify the width of the column. The unit used is the same as the default one
 * is Excel, meaning that the specified size is approximately equal to the number of characters that will fit
 * in the column.
 * <p>
 * The sizing can be applied on {@link ExcelColumn} and {@link DynamicColumns}. When {@link DynamicColumns} is annotated,
 * the specified sizing will be applied to each of the dynamic columns.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnSize {

    /**
     * Preferred column width. Specifying a negative value means that it should be calculated automatically
     * after writing all table rows by trying to adjust the width so that the longest text in that column fits.
     */
    int preferred() default -1;

    /**
     * Minimum column width. If the preferred or automatically calculated width is smaller than this value,
     * the column width will be equal to this specified minimum.
     */
    int min() default ColumnSizePreferences.MIN_WIDTH;

    /**
     * Maximum column width. If the preferred or automatically calculated width is larger than this value,
     * the column width will be equal to this specified maximum.
     */
    int max() default ColumnSizePreferences.MAX_WIDTH;
}
