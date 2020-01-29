package ua.in.khol.oleh.touristweathercomparer.model.db;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.utils.Calculation;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Place;

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

        return mAppDatabase.getCityDao().insertCity(city);
    }

    @Override
    public Maybe<City> rxGetCity(long placeId) {
        return Maybe.fromCallable(() -> mAppDatabase.getCityDao()
                .findCityByPlaceId(placeId));
    }

    @Override
    public City getCity(long placeId) {

        return mAppDatabase.getCityDao().findCityByPlaceId(placeId);
    }

    @Override
    public String getCityName(long placeId) {
        String name = mAppDatabase.getCityDao().getCityNameByLatLon(placeId);
        if (name == null)
            name = "";

        return name;
    }

    @Override
    public long getCityId(long placeId) {
        return mAppDatabase.getCityDao().getCityIdByPlaceId(placeId);
    }

    @Override
    public Completable putForecasts(List<Forecast> forecasts) {
        return Completable.fromAction(() -> mAppDatabase.getForecastDao()
                .insertList(forecasts));
    }

    @Override
    public Observable<WeatherData> observeWeatherData(double latitude, double longitude) {
        List<Observable<WeatherData>> observables = new ArrayList<>();

        return Observable.concat(observables);
    }

    @Override
    public long getForecastId(Forecast forecast, int accuracy) {
        long forecastId = mAppDatabase.getForecastDao()
                .findForecastId(forecast.getProviderId(), forecast.getPlaceId(),forecast.getDate());

        return forecastId;
    }

    @Override
    public long putForecast(Forecast forecast, int accuracy) {
        long forecastId = mAppDatabase.getForecastDao()
                .insertForecast(forecast);

        return forecastId;
    }

    @Override
    public long getPlaceId(Place place, int accuracy) {
        double lat = Calculation.round(place.getLatitude(), accuracy);
        double lon = Calculation.round(place.getLongitude(), accuracy);

        return mAppDatabase.getPlaceDao().findPlaceIdByLatLon(lat, lon);
    }

    @Override
    public long getPlaceId(double latitude, double longitude, int accuracy) {
        double lat = Calculation.round(latitude, accuracy);
        double lon = Calculation.round(longitude, accuracy);

        return mAppDatabase.getPlaceDao().findPlaceIdByLatLon(lat, lon);
    }

    @Override
    public long putPlace(Place place, int accuracy) {
        double lat = Calculation.round(place.getLatitude(), accuracy);
        double lon = Calculation.round(place.getLongitude(), accuracy);

        return mAppDatabase.getPlaceDao().insertPlace(new Place(lat, lon));
    }

    @Override
    public List<Forecast> getForecastList(long placeId) {
        return mAppDatabase.getForecastDao().queryByPlaceId(placeId);
    }

    @Override
    public void putForecastList(List<Forecast> forecastList) {
        mAppDatabase.getForecastDao().insertList(forecastList);
    }

}
