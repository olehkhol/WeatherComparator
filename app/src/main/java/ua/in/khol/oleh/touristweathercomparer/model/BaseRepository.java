package ua.in.khol.oleh.touristweathercomparer.model;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.model.location.data.CityLocation;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;

public interface BaseRepository {

    Observable<CityLocation> getSingleLocation();

    Observable<CityLocation> getLocation();

    Observable<String> getCityName();

    Observable<String> getCityName(double latitude, double longitude);

    Observable<ProviderData> getProvidersData();

    Observable<ProviderData> getProvidersData(double latitude, double longitude);

    void updatePreferences();
}
