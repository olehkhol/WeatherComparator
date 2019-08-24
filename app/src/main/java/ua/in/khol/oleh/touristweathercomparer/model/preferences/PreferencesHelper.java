package ua.in.khol.oleh.touristweathercomparer.model.preferences;

public interface PreferencesHelper {

    void putCurrentLatitude(double latitude);

    double getCurrentLatitude();

    void putCurrentLongitude(double longitude);

    double getCurrentLongitude();

    void putCurrentCityName(String cityName);

    String getCurrentCityName();

    void putCelsius(boolean celsius);

    boolean getCelsius();

    void putAccuracy(int accuracy);

    int getAccuracy();

    void putPower(int power);

    int getPower();

    void putLanguage(String language);

    String getLanguage();
}
