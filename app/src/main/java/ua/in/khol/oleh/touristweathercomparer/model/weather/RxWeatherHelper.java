package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.DarkSky;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.Owm;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.Wwo;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.Yahoo;

public class RxWeatherHelper implements WeatherHelper {

    private List<WeatherProvider> mWeatherProviders = new ArrayList<WeatherProvider>() {{
        add(new DarkSky());
        add(new Yahoo());
        add(new Owm());
        add(new Wwo()); // I got bored to refresh an api key.
        // add(new Accu()); // Up to 50 calls per day - seriously ?!
    }};

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
