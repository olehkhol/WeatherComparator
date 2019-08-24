package ua.in.khol.oleh.touristweathercomparer.model;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.model.location.data.City;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;

public interface Repository {

    Observable<City> getCity();

    Observable<ProviderData> getProvidersData();

    Observable<Boolean> getRefreshObservable();

    void update();

    void updateConfiguration();
}
