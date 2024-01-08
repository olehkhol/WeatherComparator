package ua.in.khol.oleh.touristweathercomparer.utils;

import java.util.Locale;

public class StringUtils {

    public static String capitalizeFirst(String text, Locale locale) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase(locale) + text.substring(1);
    }
}