package ua.in.khol.oleh.touristweathercomparer.model.db;

import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Location;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public class RxDatabaseHelper implements DatabaseHelper {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private final AppDatabase mAppDatabase;

    public RxDatabaseHelper(AppDatabase appDatabase) {
        mAppDatabase = appDatabase;
    }

    @Override
    public Single<Long> putLocation(Location location) {
        return Single.fromCallable(() -> mAppDatabase.getLocationDao()
                .insertLocation(location))
                .doOnSuccess(id -> Log.d(TAG, "Location lat:" + location.getLatitude()
                        + " lon:" + location.getLongitude()
                        + " successfully putted into DB with id " + id));
    }

    @Override
    public long putLocation(double latitude, double longitude) {
        return mAppDatabase.getLocationDao().insertLocation(new Location(latitude, longitude));
    }

    @Override
    public Single<Long> getLocationId(double latitude, double longitude) {
        return Single.fromCallable(() -> mAppDatabase.getLocationDao()
                .findIdByLatLon(latitude, longitude))
                .doOnSuccess(id -> Log.d(TAG, "Location with lat:" + latitude
                        + " lon:" + longitude + " has Id:" + id));
    }

    @Override
    public Single<Long> putCity(City city) {
        return Single.fromCallable(() -> mAppDatabase.getCityDao()
                .insertCity(city))
                .doOnSuccess(id -> Log.d(TAG, "City name:" + city.getName()
                        + " successfully putted into DB with id " + id));
    }

    @Override
    public Maybe<City> getCity(long locationId) {
        return Maybe.fromCallable(() -> mAppDatabase.getCityDao()
                .findByLocationId(locationId))
                .doOnSuccess(city -> Log.d(TAG, "City name:" + city.getName()
                        + " successfully receiver from DB with locationId:" + locationId));
    }

    @Override
    public Completable putForecasts(List<Forecast> forecasts) {
        return Completable.fromAction(() -> mAppDatabase.getForecastDao()
                .insertAll(forecasts))
                .doOnComplete(()
                        -> Log.d(TAG, "Forecasts successfully inserted into DB:"));
    }

    @Override
    public Single<Long> rxPutProvider(Provider provider) {

        return Single.fromCallable(() -> mAppDatabase.getProviderDao()
                .insertProvider(provider))
                .doOnSuccess(providerId -> Log.d(TAG,
                        "Provider successfully inserted into DB with Id:"
                                + providerId));
    }

    @Override
    public long putProvider(Provider provider) {
        long providerId = mAppDatabase.getProviderDao().insertProvider(provider);
        Log.d(TAG, "Provider successfully inserted into DB with Id:" + providerId);

        return providerId;
    }

    @Override
    public long putTitle(Title title) {
        long titleId = mAppDatabase.getTitleDao().insertTitle(title);
        Log.d(TAG, "Title successfully inserted into DB with Id:" + titleId);

        return titleId;
    }

    @Override
    public Maybe<List<Title>> getAllTitles(long locationId) {
        return Maybe.fromCallable(() -> mAppDatabase.getTitleDao().queryAll(locationId))
                .doOnComplete(() -> Log.d(TAG, "Titles successfully queried from DB"));
    }
}
