package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.DarkSky;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.Wwo;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.Yahoo;

public class RxWeatherHelper implements WeatherHelper {

    private List<AbstractProvider> mWeatherProviders = new ArrayList<AbstractProvider>() {{
        add(new Yahoo());
        add(new DarkSky());
        add(new Wwo());
    }};

    @Override
    public List<AbstractProvider> getWeatherProviders() {
        return mWeatherProviders;
    }

    @Override
    public Observable<ProviderData> observeProvidersData(double latitude, double longitude) {
        List<Observable<ProviderData>> observables = new ArrayList<>();

        for (AbstractProvider provider : getWeatherProviders())
            observables.add(provider.observeProviderData(latitude, longitude));

        return Observable.concat(observables);
    }

    @Override
    public List<ProviderData> getProvidersData(double latitude, double longitude) {
        List<ProviderData> providerDataList = new ArrayList<>();

        for (AbstractProvider provider : getWeatherProviders())
            providerDataList.add(provider.getProviderData(latitude, longitude));

        return providerDataList;
    }
}
