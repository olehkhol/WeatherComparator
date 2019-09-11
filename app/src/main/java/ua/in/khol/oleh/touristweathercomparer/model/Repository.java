package ua.in.khol.oleh.touristweathercomparer.model;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Location;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public interface Repository {

    Observable<Location> getLocation();

    Observable<City> getCity();

    Observable<Title> getTitle();

    Observable<Provider> getProvider();

    Observable<GodRepository.Status> getRefreshObservable();

    void update();

    void onPreferencesUpdate();

    void updateConfiguration();
}
