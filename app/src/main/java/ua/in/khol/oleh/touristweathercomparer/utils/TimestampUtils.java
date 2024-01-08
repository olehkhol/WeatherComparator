package ua.in.khol.oleh.touristweathercomparer.utils;

import java.util.Locale;

public class TimestampUtils {

    public static String toWeekYearMonthDay(int seconds, Locale locale) {
        return toWeekYearMonthDay(new Timestamp(seconds), locale);
    }

    public static String toWeekYearMonthDay(Timestamp timestamp, Locale locale) {
        return DateUtils.toWeekYearMonthDay(timestamp.asDate(locale), locale);
    }


    public static String toHourMin(int seconds, Locale locale) {
        return toHourMin(new Timestamp(seconds), locale);
    }

    public static String toHourMin(Timestamp timestamp, Locale locale) {
        return DateUtils.toHourMin(timestamp.asDate(locale), locale);
    }

    public static String toHourMinSec(int seconds, Locale locale) {
        return toHourMinSec(new Timestamp(seconds), locale);
    }

    public static String toHourMinSec(Timestamp timestamp, Locale locale) {
        return DateUtils.toHourMinSec(timestamp.asDate(locale), locale);
    }
}