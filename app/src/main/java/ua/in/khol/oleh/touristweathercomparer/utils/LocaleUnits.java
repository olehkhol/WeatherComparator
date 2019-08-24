package ua.in.khol.oleh.touristweathercomparer.utils;

/**
 * Created by Oleh Kholiavchuk.
 */

public class LocaleUnits {

    private boolean mCelsius;
    private String mLanguage;
    private static LocaleUnits sLocaleUnits;

    private LocaleUnits() {
    }

    public static LocaleUnits getInstance() {

        if (sLocaleUnits == null) {
            sLocaleUnits = new LocaleUnits();
        }
        return sLocaleUnits;
    }

    public boolean isCelsius() {
        return mCelsius;
    }

    public void setCelsius(boolean celsius) {
        mCelsius = celsius;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public static LocaleUnits getLocaleUnits() {
        return sLocaleUnits;
    }

    public static void setLocaleUnits(LocaleUnits localeUnits) {
        sLocaleUnits = localeUnits;
    }
}
