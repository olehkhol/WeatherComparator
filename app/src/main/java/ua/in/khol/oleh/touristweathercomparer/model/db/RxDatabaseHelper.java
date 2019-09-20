package ua.in.khol.oleh.touristweathercomparer.model.db;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.utils.Calculation;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public class RxDatabaseHelper implements DatabaseHelper {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private final AppDatabase mAppDatabase;

    public RxDatabaseHelper(AppDatabase appDatabase) {
        mAppDatabase = appDatabase;
    }

    @Override
    public Single<Long> rxPutCity(City city) {
        return Single.fromCallable(() -> mAppDatabase.getCityDao()
                .insertCity(city));
    }

    @Override
    public long putCity(City city, int accuracy) {
        double lat = Calculation.round(city.getLatitude(), accuracy);
        double lon = Calculation.round(city.getLongitude(), accuracy);

        return mAppDatabase.getCityDao().insertCity(new City(city.getName(), lat, lon));
    }

    @Override
    public Maybe<City> rxGetCity(double lat, double lon) {
        return Maybe.fromCallable(() -> mAppDatabase.getCityDao()
                .findCityByLatLon(lat, lon));
    }

    @Override
    public City getCity(double latitude, double longitude, int accuracy) {
        double lat = Calculation.round(latitude, accuracy);
        double lon = Calculation.round(longitude, accuracy);

        return mAppDatabase.getCityDao().findCityByLatLon(lat, lon);
    }

    @Override
    public String getCityName(double lat, double lon) {
        String name = mAppDatabase.getCityDao().findCityNameByLatLon(lat, lon);
        if (name == null)
            name = "";

        return name;
    }

    @Override
    public long getCityId(City city, int accuracy) {
        double lat = Calculation.round(city.getLatitude(), accuracy);
        double lon = Calculation.round(city.getLongitude(), accuracy);

        return mAppDatabase.getCityDao().findCityIdByLatLon(lat, lon);
    }

    @Override
    public Completable putForecasts(List<Forecast> forecasts) {
        return Completable.fromAction(() -> mAppDatabase.getForecastDao()
                .insertAll(forecasts));
    }

    @Override
    public Single<Long> rxPutProvider(Provider provider) {

        return Single.fromCallable(() -> mAppDatabase.getProviderDao()
                .insertProvider(provider));
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

    @Override
    public Observable<ProviderData> observeProvidersData(double latitude, double longitude) {
        List<Observable<ProviderData>> observables = new ArrayList<>();

        // TODO observeProvidersData

        return Observable.concat(observables);
    }

}
