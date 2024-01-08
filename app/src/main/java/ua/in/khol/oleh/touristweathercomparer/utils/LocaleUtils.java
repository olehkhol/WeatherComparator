package ua.in.khol.oleh.touristweathercomparer.utils;

import android.os.LocaleList;

import java.util.Locale;

public class LocaleUtils {

    public static Locale getSystemLocale() {
        return LocaleList.getDefault().get(0);
    }

    public static Locale getCurrentLocale(String languageTag) {
        return Locale.forLanguageTag(languageTag);
    }
}