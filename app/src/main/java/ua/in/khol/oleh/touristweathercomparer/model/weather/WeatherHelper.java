package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.List;

import io.reactivex.Observable;

public interface WeatherHelper {
    List<AbstractProvider> getWeatherProviders();

    Observable<ProviderData> observeProvidersData(double latitude, double longitude);

    List<ProviderData> getProvidersData(double latitude, double longitude);
}