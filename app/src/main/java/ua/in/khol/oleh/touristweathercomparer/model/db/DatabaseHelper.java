package ua.in.khol.oleh.touristweathercomparer.model.db;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Location;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public interface DatabaseHelper {

    Single<Long> putLocation(Location location);

    long putLocation(double latitude, double longitude);

    Single<Long> getLocationId(double latitude, double longitude);

    Single<Long> putCity(City city);

    Maybe<City> getCity(long locationId);

    Completable putForecasts(List<Forecast> forecasts);

    Single<Long> rxPutProvider(Provider provider);

    long putProvider(Provider provider);

    long putTitle(Title title);

    Maybe<List<Title>> getAllTitles(long locationId);
}
