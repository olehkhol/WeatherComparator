package ua.in.khol.oleh.touristweathercomparer.model.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_CURRENT_LATITUDE = "PREF_KEY_CURRENT_LATITUDE";
    private static final String PREF_KEY_CURRENT_LONGITUDE = "PREF_KEY_CURRENT_LONGITUDE";
    private static final String PREF_KEY_CURRENT_CITY_NAME = "PREF_KEY_CURRENT_CITY_NAME";
    private static final String PREF_KEY_CELSIUS = "PREF_KEY_CELSIUS";
    private static final String PREF_KEY_ACCURACY = "PREF_KEY_ACCURACY";
    private static final String PREF_KEY_POWER = "PREF_KEY_POWER";
    private static final String PREF_KEY_TIME = "PREF_KEY_TIME";
    private static final String PREF_KEY_LANGUAGE = "PREF_KEY_LANGUAGE";


    public static final String CELSIUS = "celsius";
    public static final String GPS_CHECK = "gps_check";
    public static final String POWER = "power";
    public static final String TIME = "time";
    public static final String LANGUAGE = "language";


    private final SharedPreferences mSharedPreferences;
    private final Editor mSharedPreferencesEditor;

    @SuppressLint("CommitPrefEdits")
    public AppPreferencesHelper(Context context, String preferencesFileName) {
        mSharedPreferences = context
                .getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    @Override
    public void putCurrentLatitude(double latitude) {
        putDouble(mSharedPreferencesEditor, PREF_KEY_CURRENT_LATITUDE, latitude).apply();
    }

    @Override
    public double getCurrentLatitude() {
        return getDouble(mSharedPreferences, PREF_KEY_CURRENT_LATITUDE, 50.4547);
    }

    @Override
    public void putCurrentLongitude(double longitude) {
        putDouble(mSharedPreferencesEditor, PREF_KEY_CURRENT_LONGITUDE, longitude).apply();
    }

    @Override
    public double getCurrentLongitude() {
        return getDouble(mSharedPreferences, PREF_KEY_CURRENT_LONGITUDE, 30.5238);
    }

    @Override
    public void putCurrentCityName(String cityName) {
        mSharedPreferencesEditor.putString(PREF_KEY_CURRENT_CITY_NAME, cityName).apply();
    }

    @Override
    public String getCurrentCityName() {
        return mSharedPreferences.getString(PREF_KEY_CURRENT_CITY_NAME, "Kyiv");
    }

    @Override
    public void putCelsius(boolean celsius) {
        mSharedPreferencesEditor.putBoolean(PREF_KEY_CELSIUS, celsius).apply();
    }

    @Override
    public boolean getCelsius() {
        return mSharedPreferences.getBoolean(PREF_KEY_CELSIUS, false);
    }

    @Override
    public void putAccuracy(int accuracy) {
        mSharedPreferencesEditor.putBoolean(PREF_KEY_ACCURACY, accuracy == 1).apply();
    }

    @Override
    public int getAccuracy() {
        return mSharedPreferences.getBoolean(PREF_KEY_ACCURACY, false) ? 1 : 2;
    }

    @Override
    public void putPower(int power) {
        mSharedPreferencesEditor.putString(PREF_KEY_POWER, String.valueOf(power)).apply();
    }

    @Override
    public int getPower() {
        return Integer.valueOf(mSharedPreferences.getString(PREF_KEY_POWER, "1"));
    }

    @Override
    public void putLanguage(String language) {
        mSharedPreferencesEditor.putString(PREF_KEY_LANGUAGE, language).apply();
    }

    @Override
    public String getLanguage() {
        return mSharedPreferences.getString(PREF_KEY_LANGUAGE, "en");
    }


    private Editor putDouble(Editor edit, String key, double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private double getDouble(SharedPreferences prefs, String key, double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
