package ua.in.khol.oleh.touristweathercomparer.helpers;

/**
 * Created by Oleh Kholiavchuk.
 */

public class LocaleUnits {

    private boolean mCelsius;
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

}
