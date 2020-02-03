package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.List;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public interface WeatherHelper {
    List<WeatherProvider> getWeatherProviders();

    Observable<WeatherData> observeWeatherData(double latitude, double longitude);

    public List<Title> getTitleList();

    public List<Provider> getProviderList();
}