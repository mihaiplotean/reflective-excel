package com.mihai.common.utils;

import org.apache.poi.util.LocaleUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        return DateFormat.getDateInstance(DateFormat.SHORT, LocaleUtil.getUserLocale());
    }

    public static Date createDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
