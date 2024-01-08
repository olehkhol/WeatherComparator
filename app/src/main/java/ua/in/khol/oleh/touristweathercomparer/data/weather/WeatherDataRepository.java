package ua.in.khol.oleh.touristweathercomparer.data.weather;

import java.util.List;

import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;

public class WeatherDataRepository implements WeatherRepository {

    WeatherProvider weatherProvider;

    public WeatherDataRepository(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    @Override
    public Maybe<Current> tryCurrent(Place place, String language, String units) {
        return weatherProvider.tryCurrent(place, language, units);
    }

    @Override
    public Maybe<List<Hourly>> tryHourlies(Place place, String language, String units) {
        return weatherProvider.tryHourlies(place, language, units);
    }
}
