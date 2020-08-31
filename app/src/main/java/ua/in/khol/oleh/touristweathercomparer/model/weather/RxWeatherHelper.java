package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Current;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Daily;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.DarkSky;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.Owm;

public class RxWeatherHelper implements WeatherHelper {
    private static final String TAG = RxWeatherHelper.class.getName();
    private final List<WeatherProvider> mWeatherProviders = new ArrayList<WeatherProvider>() {{
        add(new Owm(2));
        add(new DarkSky(0));
    }};

    public RxWeatherHelper() {
    }

    @Override
    public List<Current> getCurrents(Place place, int time) {
        List<Current> currents = new ArrayList<>();
        long placeId = place.getId();
        double latitude = place.getLatitude();
        double longitude = place.getLongitude();
        String language = place.getLanguage();

        for (WeatherProvider provider : mWeatherProviders) {
            int providerId = provider.getId();
            Forecast data = provider.getCurrent(latitude, longitude, language);
            if (data != null) {
                Current current = Current.fromForecast(providerId, placeId, data);
                // cast to the same time avoiding cached network data
                currents.add(current);
            }
        }

        return currents;
    }


    /**
     * Fetch a raw list of all possible daily forecasts
     *
     * @param place - {@link Place}
     * @param date  - keep it for future
     * @return - forecasts data for all weather providers
     */
    @Override
    public List<Daily> getDailies(Place place, int date) {
        List<Daily> dailies = new ArrayList<>();
        long placeId = place.getId();
        double latitude = place.getLatitude();
        double longitude = place.getLongitude();
        String language = place.getLanguage();

        for (WeatherProvider provider : mWeatherProviders) {
            int providerId = provider.getId();
            List<Forecast> datas = provider.getDaily(latitude, longitude, language);
            if (datas != null)
                for (Forecast data : datas)
                    dailies.add(toForecast(providerId, placeId, data));
        }

        return dailies;
    }

    @Override
    public Maybe<List<Current>> tryCurrents(Place place, int time) {
        return Maybe.fromCallable(() -> getCurrents(place, time))
                .filter(currents -> currents.size() > 0);
    }

    @Override
    public MaybeSource<List<Daily>> tryDailies(Place place, int date) {
        return Maybe.fromCallable(() -> getDailies(place, date))
                .filter(forecasts -> forecasts.size() > 0);
    }

    private Daily toForecast(int providerId, long placeId, Forecast forecast) {
        Daily daily = new Daily(providerId, placeId);

        daily.setDate(forecast.getDate());
        daily.setLow(forecast.getLow());
        daily.setHigh(forecast.getHigh());
        daily.setPressure(forecast.getPressure());
        daily.setSpeed(forecast.getSpeed());
        daily.setDegree(forecast.getDegree());
        daily.setHumidity(forecast.getHumidity());
        daily.setText(forecast.getText());
        daily.setImage(forecast.getSrc());

        return daily;
    }
}
