package ua.in.khol.oleh.touristweathercomparer.model;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public interface Repository {
    Observable<City> observeCity();

    Observable<Title> observeTitle();

    Observable<Provider> observeProvider();

    Observable<Forecast> observeForecast();

    Observable<GodRepository.Status> observeStatus();

    void clearStatus();

    void update();

    void cancel();

    void onPreferencesUpdate();

    void updateConfiguration();

}
