package com.mihai.writer.style;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatUtils {

    public static final int DEFAULT_DATE_FORMAT_INDEX = 14;

    private DateFormatUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getLocalizedDatePattern(String fallBack) {
        DateFormat dateFormat = getLocalizedDateFormat();
        if(dateFormat instanceof SimpleDateFormat simpleDateFormat) {
            return simpleDateFormat.toPattern();
        }
        return fallBack;
    }

    public static DateFormat getLocalizedDateFormat() {
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    }
}
