package ua.in.khol.oleh.touristweathercomparer.model.db;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Place;

public interface DatabaseHelper {

    Single<Long> rxPutCity(City city);

    long putCity(City city, int accuracy);

    Maybe<City> rxGetCity(long placeId);

    City getCity(long placeId);

    String getCityName(long placeId);

    long getCityId(long placeId);

    Completable putForecasts(List<Forecast> forecasts);

    Observable<WeatherData> observeWeatherData(double latitude, double longitude);

    long getForecastId(Forecast forecast, int accuracy);

    long putForecast(Forecast forecast, int accuracy);

    long getPlaceId(Place place, int accuracy);

    long getPlaceId(double latitude, double longitude, int accuracy);

    long putPlace(Place place, int accuracy);

    List<Forecast> getForecastList(long placeId);

    List<Forecast> getForecastList(int providerId, long placeId, int date);

    void putForecastList(List<Forecast> forecastList);
}
