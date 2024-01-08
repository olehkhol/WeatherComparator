package ua.in.khol.oleh.touristweathercomparer.utils;

import static ua.in.khol.oleh.touristweathercomparer.utils.StringUtils.capitalizeFirst;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String FULL_MONTH_DAY_NUMBER = "MMMM d";
    private static final String WEEK_DAY_MONTH_NUMBER = "dd.MM";
    private static final String WEEK_DAY_MONTH_YEAR = "EEEE, dd.MM.yyyy";
    private static final String HOUR_MINUTE = "HH:mm";
    private static final String HOUR_MINUTE_SEC = "HH:mm:ss";

    public static String toFullMonthDayNumber(Date date, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(FULL_MONTH_DAY_NUMBER, locale);
        return capitalizeFirst(sdf.format(date), locale);
    }

    public static String toWeekMonthDay(Date date, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(WEEK_DAY_MONTH_NUMBER, locale);
        return sdf.format(date);
    }

    public static String toWeekYearMonthDay(Date date, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(WEEK_DAY_MONTH_YEAR, locale);
        return sdf.format(date);
    }

    public static String toHourMin(Date date, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(HOUR_MINUTE, locale);
        return sdf.format(date);
    }

    public static String toHourMinSec(Date date, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(HOUR_MINUTE_SEC, locale);
        return sdf.format(date);
    }
}
