package ua.in.khol.oleh.touristweathercomparer.utils;

import java.util.Locale;

public class LocaleUtils {

    public static Locale getCurrentLocale(String languageTag) {
        return Locale.forLanguageTag(languageTag);
    }
}