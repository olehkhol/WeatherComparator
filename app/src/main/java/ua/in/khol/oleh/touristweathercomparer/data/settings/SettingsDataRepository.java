package ua.in.khol.oleh.touristweathercomparer.data.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ua.in.khol.oleh.touristweathercomparer.R;

public class SettingsDataRepository implements SettingsRepository {

    private static final String LANGUAGE_KEY = "LANGUAGE_KEY";
    private static final String THEME_CODE_KEY = "THEME_CODE_KEY";
    private static final String THEME_CODE_VALUE = "0";
    private static final String UNITS_KEY = "UNITS_KEY";
    private static final String UNITS_VALUE = "metric";

    private final PublishSubject<String> preferencesChangePublisher = PublishSubject.create();
    private final SharedPreferences.OnSharedPreferenceChangeListener listener = (preferences, key) -> {
        if (key != null) preferencesChangePublisher.onNext(key);
    };
    private final SharedPreferences sharedPreferences;
    private final Observable<String> preferenceObservable;
    private final String[] languages;

    public SettingsDataRepository(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferenceObservable = preferencesChangePublisher
                .doOnSubscribe(disposable ->
                        sharedPreferences.registerOnSharedPreferenceChangeListener(listener))
                .doOnDispose(() ->
                        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener));

        languages = context.getResources().getStringArray(R.array.language_codes);
    }

    @Override
    public synchronized void storeLanguage(String code) {
        sharedPreferences.edit().putString(LANGUAGE_KEY, code).apply();
    }

    @Override
    public synchronized String retrieveLanguage() {
        String language;
        if ((language = sharedPreferences.getString(LANGUAGE_KEY, "")).isEmpty()) {
            String defaultCode = Locale.getDefault().getLanguage();
            language = languages[0];
            for (String code : languages)
                if (code.equals(defaultCode)) {
                    language = defaultCode;
                    break;
                }

            storeLanguage(language);
        }

        return language;
    }

    @Override
    public synchronized void storeThemeCode(int code) {
        sharedPreferences.edit().putString(THEME_CODE_KEY, String.valueOf(code)).apply();
    }

    @Override
    public synchronized int retrieveThemeCode() {
        int themeCode;
        String themeCodeValue = sharedPreferences.getString(THEME_CODE_KEY, THEME_CODE_VALUE);
        if (THEME_CODE_VALUE.equals(themeCodeValue))
            storeThemeCode(themeCode = -1);
        else
            themeCode = Integer.parseInt(themeCodeValue);

        return themeCode;
    }

    @Override
    public String retrieveUnits() {
        return sharedPreferences.getString(UNITS_KEY, UNITS_VALUE);
    }

    private Observable<String> getStringObservable(final String key, final String defaultValue) {
        return preferenceObservable
                .filter(key::equals)
                .map(k -> sharedPreferences.getString(k, defaultValue));
    }

    private Observable<Integer> getIntegerObservable(final String key, final int defaultValue) {
        return preferenceObservable
                .filter(key::equals)
                .map(k -> sharedPreferences.getInt(k, defaultValue));
    }

    @Override
    public Observable<String> getLanguageCodeObservable() {
        return getStringObservable(LANGUAGE_KEY, retrieveLanguage());
    }

    @Override
    public Observable<Integer> getThemeCodeObservable() {
        return getStringObservable(THEME_CODE_KEY, String.valueOf(retrieveThemeCode()))
                .map(Integer::parseInt);
    }
}