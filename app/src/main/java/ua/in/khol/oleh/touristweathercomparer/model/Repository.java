package ua.in.khol.oleh.touristweathercomparer.model;

import java.util.List;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Place;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public interface Repository {
    enum Status {
        OFFLINE, CRITICAL_OFFLINE, ONLINE, REFRESHING, REFRESHED, NEED_RECREATE, ERROR,
        LOCATION_UNAVAILABLE, CLEAR
    }

    List<Title> getTitleList();

    List<Provider> getProviderList();

    Observable<Place> observePlace();

    Observable<City> observeCity();

    Observable<Forecast> observeForecast();

    Observable<Status> observeStatus();

    void clearStatus();

    void update();

    void cancel();

    void updatePreferences();

    void updateConfiguration();

    boolean getPrefCelsius();

    int getPrefLanguageIndex();

    void putPrefCelsius(boolean celsius);

    void putPrefLanguageIndex(int index);
}
