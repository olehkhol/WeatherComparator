package ua.in.khol.oleh.touristweathercomparer.model.db;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public interface DatabaseHelper {

    Single<Long> rxPutCity(City city);

    long putCity(City city, int accuracy);

    Maybe<City> rxGetCity(double lat, double lon);

    City getCity(double lat, double lon, int accuracy);

    String getCityName(double lat, double lon);

    long getCityId(City city, int accuracy);

    Completable putForecasts(List<Forecast> forecasts);

    Single<Long> rxPutProvider(Provider provider);

    long putProvider(Provider provider);

    long putTitle(Title title);

    Maybe<List<Title>> getAllTitles(long locationId);

    Observable<ProviderData> observeProvidersData(double latitude, double longitude);

}
