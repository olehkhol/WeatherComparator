package ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap;

import static ua.in.khol.oleh.touristweathercomparer.Secrets.OPEN_WEATHER_API_KEY;
import static ua.in.khol.oleh.touristweathercomparer.Globals.OPEN_WEATHER_API_URL;
import static ua.in.khol.oleh.touristweathercomparer.Globals.OPEN_WEATHER_NAME;
import static ua.in.khol.oleh.touristweathercomparer.Globals.OPEN_WEATHER_SITE;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;
import ua.in.khol.oleh.touristweathercomparer.data.weather.WeatherProvider;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Main;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Weather;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Wind;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.hourly.Three;

public class OpenWeatherProvider extends WeatherProvider {

    private final OpenWeatherService openWeatherService;

    public OpenWeatherProvider() {
        super(OPEN_WEATHER_NAME, OPEN_WEATHER_SITE, OPEN_WEATHER_API_URL);

        openWeatherService = createService(OpenWeatherService.class);
    }

    @Override
    public Maybe<Current> tryCurrent(final Place place, final String language, final String units) {
        return openWeatherService
                .tryCurrent(
                        String.valueOf(place.latitude),
                        String.valueOf(place.longitude),
                        language,
                        units,
                        OPEN_WEATHER_API_KEY
                )
                .map(currentModel -> {
                    Main main = currentModel.main;
                    Wind wind = currentModel.wind;
                    Weather weather = currentModel.weather.get(0);
                    Current current = new Current();

                    current.latitude = place.latitude;
                    current.longitude = place.longitude;
                    current.language = language;
                    current.units = units;
                    current.date = currentModel.dt;
                    current.temp = main.temp;
                    current.pressure = main.pressure;
                    current.speed = wind.speed;
                    current.degree = wind.deg;
                    current.humidity = main.humidity;
                    current.description = weather.description;
                    current.image = getPath() + weather.icon;

                    return current;
                });
    }

    @Override
    public Maybe<List<Hourly>> tryHourlies(final Place place, final String language, final String units) {
        return openWeatherService
                .tryHourlies(
                        String.valueOf(place.latitude),
                        String.valueOf(place.longitude),
                        language,
                        units,
                        OPEN_WEATHER_API_KEY
                )
                .map(hourlyModel -> {
                    List<Hourly> hourlies = new ArrayList<>();

                    for (Three three : hourlyModel.threes) {
                        Main main = three.main;
                        Wind wind = three.wind;
                        Weather weather = three.weather.get(0);
                        Hourly hourly = new Hourly();

                        hourly.latitude = place.latitude;
                        hourly.longitude = place.longitude;
                        hourly.language = language;
                        hourly.units = units;
                        hourly.date = three.dt;
                        hourly.tempMin = main.tempMin;
                        hourly.tempMax = main.tempMax;
                        hourly.pressure = main.pressure;
                        hourly.speed = wind.speed;
                        hourly.degree = wind.deg;
                        hourly.humidity = main.humidity;
                        hourly.description = weather.description;
                        hourly.image = getPath() + weather.icon;

                        hourlies.add(hourly);
                    }

                    return hourlies;
                });
    }
}