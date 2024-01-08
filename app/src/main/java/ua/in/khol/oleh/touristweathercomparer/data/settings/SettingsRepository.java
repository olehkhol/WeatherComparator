package ua.in.khol.oleh.touristweathercomparer.data.settings;

import io.reactivex.Observable;

public interface SettingsRepository {

    void storeLanguage(String language);

    String retrieveLanguage();

    void storeThemeCode(int code);

    int retrieveThemeCode();

    String retrieveUnits();

    Observable<String> getLanguageCodeObservable();

    Observable<Integer> getThemeCodeObservable();
}