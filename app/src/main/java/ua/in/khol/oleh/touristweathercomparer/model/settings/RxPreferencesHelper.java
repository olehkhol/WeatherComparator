package ua.in.khol.oleh.touristweathercomparer.model.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;

public class RxPreferencesHelper implements RxPreferences {
    private static final String PREF_KEY_LANGUAGE_INDEX = "PREF_KEY_LANGUAGE_INDEX";
    private static final String PREF_KEY_PRESSURE_INDEX = "PREF_KEY_PRESSURE_INDEX";
    private static final String PREF_KEY_SPEED_INDEX = "PREF_KEY_SPEED_INDEX";
    private static final String PREF_KEY_TEMPERATURE_INDEX = "PREF_KEY_TEMPERATURE_INDEX";
    private static final String PREF_KEY_LATITUDE = "PREF_KEY_LATITUDE";
    private static final String PREF_KEY_LONGITUDE = "PREF_KEY_LONGITUDE";
    private static final String PREF_KEY_CURRENT_TIMESTAMP = "PREF_KEY_CURRENT_TIMESTAMP";
    private static final String PREF_KEY_DAILIES_TIMESTAMP = "PREF_KEY_DAILIES_TIMESTAMP";

    private static List<String> mLanguages;
    private final SharedPreferences mPreferences;
    private final SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    public RxPreferencesHelper(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPreferences.edit();
        mLanguages = Arrays.asList(context.getResources().getStringArray(R.array.language_values));
    }

    @Override
    public String getLanguage() {
        return mLanguages.get(getLanguageIndex());
    }

    @Override
    public void putLanguage(String language) {
        int index = mLanguages.indexOf(language);
        if (index == -1)
            index = 0;
        mEditor.putInt(PREF_KEY_LANGUAGE_INDEX, index).apply();
    }

    @Override
    public int getLanguageIndex() {
        int index = 0;
        if (mPreferences.contains(PREF_KEY_LANGUAGE_INDEX))
            index = mPreferences.getInt(PREF_KEY_LANGUAGE_INDEX, 0);
        else {
            String language = Locale.getDefault().getLanguage();
            if (mLanguages.contains(language))
                index = mLanguages.indexOf(language);
            putLanguageIndex(index);
        }

        return index;
    }

    @Override
    public void putLanguageIndex(int index) {
        mEditor.putInt(PREF_KEY_LANGUAGE_INDEX, index).apply();
    }

    @Override
    public int getPressureIndex() {
        int index = 0;
        if (mPreferences.contains(PREF_KEY_PRESSURE_INDEX))
            index = mPreferences.getInt(PREF_KEY_PRESSURE_INDEX, 0);
        else
            putPressureIndex(index);

        return index;
    }

    @Override
    public void putPressureIndex(int index) {
        mEditor.putInt(PREF_KEY_PRESSURE_INDEX, index).apply();
    }

    @Override
    public int getTemperatureIndex() {
        int index = 0;
        if (mPreferences.contains(PREF_KEY_TEMPERATURE_INDEX))
            index = mPreferences.getInt(PREF_KEY_TEMPERATURE_INDEX, 0);
        else
            putTemperatureIndex(index);

        return index;
    }

    @Override
    public void putTemperatureIndex(int index) {
        mEditor.putInt(PREF_KEY_TEMPERATURE_INDEX, index).apply();
    }

    @Override
    public int getSpeedIndex() {
        int index = 0;
        if (mPreferences.contains(PREF_KEY_SPEED_INDEX))
            index = mPreferences.getInt(PREF_KEY_SPEED_INDEX, 0);
        else
            putSpeedIndex(index);

        return index;
    }

    @Override
    public void putSpeedIndex(int index) {
        mEditor.putInt(PREF_KEY_SPEED_INDEX, index).apply();
    }

    @Override
    public double getLat() {
        double latitude = 0f;
        if (mPreferences.contains(PREF_KEY_LATITUDE))
            return getDouble(mPreferences, PREF_KEY_LATITUDE, latitude);
        else
            putLat(latitude);

        return latitude;
    }

    @Override
    public void putLat(double lat) {
        putDouble(mEditor, PREF_KEY_LATITUDE, lat).apply();
    }

    @Override
    public double getLon() {
        double longitude = 0f;
        if (mPreferences.contains(PREF_KEY_LONGITUDE))
            return getDouble(mPreferences, PREF_KEY_LONGITUDE, longitude);
        else
            putLon(longitude);

        return longitude;
    }

    @Override
    public void putLon(double lon) {
        putDouble(mEditor, PREF_KEY_LONGITUDE, lon).apply();
    }

    @Override
    public void putSetting(Settings settings) {
        putLanguageIndex(settings.getLanguageIndex());
        putPressureIndex(settings.getPressureIndex());
        putTemperatureIndex(settings.getTemperatureIndex());
        putSpeedIndex(settings.getSpeedIndex());
    }

    @Override
    public Settings getSettings() {
        return new Settings(getLanguageIndex(), getPressureIndex(),
                getTemperatureIndex(), getSpeedIndex());
    }

    @Override
    public Maybe<LatLon> tryLatLon() {
        return Maybe.fromCallable(() -> {
            if (mPreferences.contains(PREF_KEY_LATITUDE)
                    && mPreferences.contains(PREF_KEY_LONGITUDE))
                return new LatLon(getDouble(mPreferences, PREF_KEY_LATITUDE, 0),
                        getDouble(mPreferences, PREF_KEY_LONGITUDE, 0));

            return null;
        });
    }

    @Override
    public void putLatLon(LatLon latLon) {
        putDouble(mEditor, PREF_KEY_LATITUDE, latLon.getLat()).apply();
        putDouble(mEditor, PREF_KEY_LONGITUDE, latLon.getLon()).apply();
    }

    @Override
    public LatLon getLatLon() {
        return new LatLon(getDouble(mPreferences, PREF_KEY_LATITUDE, 0),
                getDouble(mPreferences, PREF_KEY_LONGITUDE, 0));
    }

    private double getDouble(SharedPreferences prefs, String key, double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    private SharedPreferences.Editor putDouble(SharedPreferences.Editor edit,
                                               String key, double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }
}
