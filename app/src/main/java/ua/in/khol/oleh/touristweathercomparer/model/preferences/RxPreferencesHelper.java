package ua.in.khol.oleh.touristweathercomparer.model.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ua.in.khol.oleh.touristweathercomparer.R;

public class RxPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_CELSIUS = "PREF_KEY_CELSIUS";
    private static final String PREF_KEY_LANGUAGE_INDEX = "PREF_KEY_LANGUAGE_INDEX";

    private static List<String> mLanguages;
    private final SharedPreferences mSharedPreferences;
    private final Editor mSharedPreferencesEditor;

    @SuppressLint("CommitPrefEdits")
    public RxPreferencesHelper(Context context, String preferencesFileName) {
        mSharedPreferences = context
                .getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mLanguages = Arrays.asList(context.getResources().getStringArray(R.array.languages_values));
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
    public void putLanguage(String language) {
        int index = mLanguages.indexOf(language);
        if (index == -1)
            index = 0;
        mSharedPreferencesEditor.putInt(PREF_KEY_LANGUAGE_INDEX, index).apply();
    }

    @Override
    public String getLanguage() {
        return mLanguages.get(getLanguageIndex());
    }

    @Override
    public void putLanguageIndex(int index) {
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

    private Editor putDouble(Editor edit, String key, double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private double getDouble(SharedPreferences prefs, String key, double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
