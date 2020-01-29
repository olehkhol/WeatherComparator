package ua.in.khol.oleh.touristweathercomparer.model.preferences;

public interface PreferencesHelper {

    void putCelsius(boolean celsius);

    boolean getCelsius();

    void putLanguage(String language);

    String getLanguage();

    int getLanguageIndex();

    void putLanguageIndex(int index);
}
