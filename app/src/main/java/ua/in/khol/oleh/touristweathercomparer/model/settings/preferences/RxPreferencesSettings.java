package ua.in.khol.oleh.touristweathercomparer.model.settings.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.model.settings.RxSettings;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;

public class RxPreferencesSettings implements RxSettings {
    private static final String PREF_KEY_LANGUAGE_INDEX = "PREF_KEY_LANGUAGE_INDEX";
    private static final String PREF_KEY_PRESSURE_INDEX = "PREF_KEY_PRESSURE_INDEX";
    private static final String PREF_KEY_SPEED_INDEX = "PREF_KEY_SPEED_INDEX";
    private static final String PREF_KEY_TEMPERATURE_INDEX = "PREF_KEY_TEMPERATURE_INDEX";
    private static final String PREF_KEY_LATITUDE = "PREF_KEY_LATITUDE";
    private static final String PREF_KEY_LONGITUDE = "PREF_KEY_LONGITUDE";

    private static List<String> mLanguages;
    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences.Editor mSharedPreferencesEditor;

    @SuppressLint("CommitPrefEdits")
    public RxPreferencesSettings(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mLanguages = Arrays.asList(context.getResources().getStringArray(R.array.languages_values));
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
        mSharedPreferencesEditor.putInt(PREF_KEY_LANGUAGE_INDEX, index).apply();
    }

    @Override
    public int getLanguageIndex() {
        int index = 0;
        if (mSharedPreferences.contains(PREF_KEY_LANGUAGE_INDEX))
            index = mSharedPreferences.getInt(PREF_KEY_LANGUAGE_INDEX, 0);
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
        mSharedPreferencesEditor.putInt(PREF_KEY_LANGUAGE_INDEX, index).apply();
    }

    @Override
    public int getPressureIndex() {
        int index = 0;
        if (mSharedPreferences.contains(PREF_KEY_PRESSURE_INDEX))
            index = mSharedPreferences.getInt(PREF_KEY_PRESSURE_INDEX, 0);
        else
            putPressureIndex(index);

        return index;
    }

    @Override
    public void putPressureIndex(int index) {
        mSharedPreferencesEditor.putInt(PREF_KEY_PRESSURE_INDEX, index).apply();
    }

    @Override
    public int getTemperatureIndex() {
        int index = 0;
        if (mSharedPreferences.contains(PREF_KEY_TEMPERATURE_INDEX))
            index = mSharedPreferences.getInt(PREF_KEY_TEMPERATURE_INDEX, 0);
        else
            putTemperatureIndex(index);

        return index;
    }

    @Override
    public void putTemperatureIndex(int index) {
        mSharedPreferencesEditor.putInt(PREF_KEY_TEMPERATURE_INDEX, index).apply();
    }

    @Override
    public int getSpeedIndex() {
        int index = 0;
        if (mSharedPreferences.contains(PREF_KEY_SPEED_INDEX))
            index = mSharedPreferences.getInt(PREF_KEY_SPEED_INDEX, 0);
        else
            putSpeedIndex(index);

        return index;
    }

    @Override
    public void putSpeedIndex(int index) {
        mSharedPreferencesEditor.putInt(PREF_KEY_SPEED_INDEX, index).apply();
    }

    @Override
    public double getLat() {
        double latitude = 0f;
        if (mSharedPreferences.contains(PREF_KEY_LATITUDE))
            return getDouble(mSharedPreferences, PREF_KEY_LATITUDE, latitude);
        else
            putLat(latitude);

        return latitude;
    }

    @Override
    public void putLat(double lat) {
        putDouble(mSharedPreferencesEditor, PREF_KEY_LATITUDE, lat).apply();
    }

    @Override
    public double getLon() {
        double longitude = 0f;
        if (mSharedPreferences.contains(PREF_KEY_LONGITUDE))
            return getDouble(mSharedPreferences, PREF_KEY_LONGITUDE, longitude);
        else
            putLon(longitude);

        return longitude;
    }

    @Override
    public void putLon(double lon) {
        putDouble(mSharedPreferencesEditor, PREF_KEY_LONGITUDE, lon).apply();
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

    private double getDouble(SharedPreferences prefs, String key, double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    private SharedPreferences.Editor putDouble(SharedPreferences.Editor edit,
                                               String key, double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }
}
