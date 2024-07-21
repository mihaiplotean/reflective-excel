package com.mihai.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated field corresponds to multiple grouped columns. The annotated type can contain fixed,
 * dynamic and grouped columns.
 * <pre>
 * For example, suppose that we have in the sheet:
 *      +---------------------+
 *      |      Group name     |
 *      +----------+----------+
 *      | Column A | Column B |
 *      +----------+----------+
 *
 * The object representation will be as follows:
 * {@code
 * public class Group {
 *
 *     @ExcelCellGroup("Group name")
 *     private SubGroup subGroup;
 *
 *     public class SubGroup {
 *
 *         @ExcelColumn("Column A")
 *         private String valueA;
 *
 *         @ExcelColumn("Column B")
 *         private String valueB;
 *     }
 * }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelCellGroup {

    String value();
}

