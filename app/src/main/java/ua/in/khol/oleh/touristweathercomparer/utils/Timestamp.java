package ua.in.khol.oleh.touristweathercomparer.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Timestamp {
    private static final long SECOND_IN_MILLIS = 1000L;
    private final long seconds;

    public Timestamp(long seconds) {
        this.seconds = seconds;
    }

    public Date asDate(Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTimeInMillis(seconds * SECOND_IN_MILLIS);

        return calendar.getTime();
    }

    public static long now(Locale locale) {
        return Calendar.getInstance(locale).getTimeInMillis() / SECOND_IN_MILLIS;
    }
}
