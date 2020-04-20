package ua.in.khol.oleh.touristweathercomparer.model.db;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

public interface DatabaseHelper {
    // Place
    long getPlaceId(Place place);

    long putPlace(Place place);

    Place getPlace(LatLon latLon, String lang);

    // Forecasts
    Completable putForecastsCompletable(List<Forecast> forecasts);

    Observable<WeatherData> observeWeatherData(double latitude, double longitude);

    long getForecastId(Forecast forecast);

    long putForecast(Forecast forecast);

    List<Forecast> getDailies(long placeId, int date);

    void putDailies(List<Forecast> dailies);

    Place getPlace(double lat, double lon, String lang);

    Forecast getCurrent(long placeId, int providerId, int date);

    long putCurrent(Forecast current);

    List<Forecast> getCurrents(long place, int date);

    void putCurrents(List<Forecast> currents);
}
