package com.mihai.core.annotation;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public @interface ExcelCellStyle {

    String format() default "";

    HorizontalAlignment horizontalAlignment() default HorizontalAlignment.GENERAL;

    VerticalAlignment verticalAlignment() default VerticalAlignment.BOTTOM;

    BorderStyle borderStyle() default BorderStyle.NONE;

    String hexBackgroundColor() default "";

    String hexBorderColor() default "";

    String fontName() default "";

    int fontSize() default -1;

    boolean boldText() default false;

    boolean italicText() default false;

    boolean underlineText() default false;

    boolean wrapText() default false;
}
