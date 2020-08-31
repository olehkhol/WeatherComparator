package ua.in.khol.oleh.touristweathercomparer.model.settings;

import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;

public interface RxPreferences {

    String getLanguage();

    void putLanguage(String language);

    int getLanguageIndex();

    void putLanguageIndex(int index);

    int getPressureIndex();

    void putPressureIndex(int index);

    int getTemperatureIndex();

    void putTemperatureIndex(int index);

    int getSpeedIndex();

    void putSpeedIndex(int index);

    double getLat();

    void putLat(double lat);

    double getLon();

    void putLon(double lon);

    void putSetting(Settings settings);

    Settings getSettings();

    Maybe<LatLon> tryLatLon();

    void putLatLon(LatLon latLon);

    LatLon getLatLon();
}
