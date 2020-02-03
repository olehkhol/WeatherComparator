package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.DarkSky;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.Owm;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.Wwo;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.Yahoo;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public class RxWeatherHelper implements WeatherHelper {

    private List<WeatherProvider> mWeatherProviders;
    private List<Title> mTitleList = new ArrayList<>();
    private List<Provider> mProviderList = new ArrayList<>();

    public RxWeatherHelper() {
        mWeatherProviders = new ArrayList<WeatherProvider>() {{
            add(new DarkSky());
            add(new Yahoo());
            add(new Owm());
            add(new Wwo());
        }};
        for (WeatherProvider wp : mWeatherProviders) {
            mTitleList.add(new Title(wp.getId(), wp.getName()));
            mProviderList.add(new Provider(wp.getId(), wp.getSite(), wp.getBanner()));
        }
    }

    @Override
    public List<Title> getTitleList() {
        return mTitleList;
    }

    @Override
    public List<Provider> getProviderList() {
        return mProviderList;
    }

    @Override
    public List<WeatherProvider> getWeatherProviders() {
        return mWeatherProviders;
    }

    @Override
    public Observable<WeatherData> observeWeatherData(double latitude, double longitude) {
        List<Observable<WeatherData>> observables = new ArrayList<>();

        for (WeatherProvider provider : getWeatherProviders())
            observables.add(provider.observeWeatherData(latitude, longitude));

        return Observable.concat(observables);
    }
}
