package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.ArrayList;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.model.db.data.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.DarkSky;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.Owm;

public class RxWeatherHelper implements WeatherHelper {
    private final List<WeatherProvider> mWeatherProviders = new ArrayList<WeatherProvider>() {{
        add(new Owm(2));
        add(new DarkSky(0));
    }};

    public RxWeatherHelper() {
    }

    @Override
    public List<Forecast> getCurrents(Place place, int time) {
        List<Forecast> currents = new ArrayList<>();
        long placeId = place.getId();
        double latitude = place.getLatitude();
        double longitude = place.getLongitude();
        String language = place.getLanguage();

        for (WeatherProvider provider : mWeatherProviders) {
            int providerId = provider.getId();
            WeatherData data = provider.getCurrent(latitude, longitude, language);
            if (data != null) {
                Forecast forecast = toForecast(providerId, placeId, data);
                // cast to the same time avoiding cached network data
                forecast.setDate(time);
                currents.add(forecast);
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
    public List<Forecast> getDailies(Place place, int date) {
        List<Forecast> dailies = new ArrayList<>();
        long placeId = place.getId();
        double latitude = place.getLatitude();
        double longitude = place.getLongitude();
        String language = place.getLanguage();

        for (WeatherProvider provider : mWeatherProviders) {
            int providerId = provider.getId();
            List<WeatherData> datas = provider.getDaily(latitude, longitude, language);
            if (datas != null)
                for (WeatherData data : datas)
                    dailies.add(toForecast(providerId, placeId, data));
        }

        return dailies;
    }

    private Forecast toForecast(int providerId, long placeId, WeatherData weatherData) {
        Forecast forecast = new Forecast(providerId, placeId);

        forecast.setDate(weatherData.getDate());
        forecast.setLow(weatherData.getLow());
        forecast.setHigh(weatherData.getHigh());
        forecast.setPressure(weatherData.getPressure());
        forecast.setSpeed(weatherData.getSpeed());
        forecast.setDegree(weatherData.getDegree());
        forecast.setHumidity(weatherData.getHumidity());
        forecast.setText(weatherData.getText());
        forecast.setImage(weatherData.getSrc());
        forecast.setCurrent(weatherData.isCurrent());

        return forecast;
    }
}
