package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.List;

import io.reactivex.Observable;

public interface WeatherHelper {
    List<WeatherProvider> getWeatherProviders();

    Observable<WeatherData> observeWeatherData(double latitude, double longitude);
}