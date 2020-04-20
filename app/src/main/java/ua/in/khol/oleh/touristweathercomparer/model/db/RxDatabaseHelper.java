package ua.in.khol.oleh.touristweathercomparer.model.db;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

public class RxDatabaseHelper implements DatabaseHelper {

    private final AppDatabase mAppDatabase;

    public RxDatabaseHelper(AppDatabase appDatabase) {
        mAppDatabase = appDatabase;
    }

    @Override
    public long getPlaceId(Place place) {
        return mAppDatabase.getPlaceDao()
                .findPlaceIdByLatLon(place.getLatitude(), place.getLongitude());
    }

    @Override
    public long putPlace(Place place) {
        return mAppDatabase.getPlaceDao().insertPlace(place);
    }

    @Override
    public Place getPlace(LatLon latLon, String lang) {
        return mAppDatabase.getPlaceDao().findByLatLon(latLon.getLat(), latLon.getLon(), lang);
    }

    @Override
    public Completable putForecastsCompletable(List<Forecast> forecasts) {
        return Completable.fromAction(() -> mAppDatabase.getForecastDao()
                .insertForecasts(forecasts));
    }

    @Override
    public Observable<WeatherData> observeWeatherData(double latitude, double longitude) {
        List<Observable<WeatherData>> observables = new ArrayList<>();

        return Observable.concat(observables);
    }

    @Override
    public long getForecastId(Forecast forecast) {
        return mAppDatabase.getForecastDao()
                .findForecastId(forecast.getProviderId(), forecast.getPlaceId(), forecast.getDate());
    }

    @Override
    public long putForecast(Forecast forecast) {
        return mAppDatabase.getForecastDao().insertForecast(forecast);
    }

    @Override
    public List<Forecast> getDailies(long placeId, int date) {
        return mAppDatabase.getForecastDao().queryFromDate(placeId, date, false);
    }

    @Override
    public void putDailies(List<Forecast> dailies) {
        mAppDatabase.getForecastDao().insertForecasts(dailies);
    }

    @Override
    public Place getPlace(double lat, double lon, String lang) {
        return mAppDatabase.getPlaceDao().findByLatLon(lat, lon, lang);
    }

    @Override
    public Forecast getCurrent(long placeId, int providerId, int date) {
        List<Forecast> currents
                = mAppDatabase.getForecastDao().queryFromDate(providerId, placeId, date, true);

        int size = currents.size();
        if (size == 0)
            return null;

        return currents.get(size - 1);
    }

    @Override
    public long putCurrent(Forecast current) {
        return mAppDatabase.getForecastDao().insertForecast(current);
    }

    @Override
    public List<Forecast> getCurrents(long placeId, int date) {
        List<Forecast> currents
                = mAppDatabase.getForecastDao().queryByPlaceFromDate(placeId, date, true);
        if (currents.size() > 0)
            return currents;

        return null;
    }

    @Override
    public void putCurrents(List<Forecast> currents) {
        mAppDatabase.getForecastDao().insertForecasts(currents);
    }
}
