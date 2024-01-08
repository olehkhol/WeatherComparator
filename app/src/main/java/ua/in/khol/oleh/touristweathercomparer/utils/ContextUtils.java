package ua.in.khol.oleh.touristweathercomparer.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class ContextUtils {

    public static Context getLocalizedContext(Context context, Locale locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }
}