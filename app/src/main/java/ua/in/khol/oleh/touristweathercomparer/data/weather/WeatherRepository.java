package ua.in.khol.oleh.touristweathercomparer.data.weather;

import java.util.List;

import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;

public interface WeatherRepository {

    Maybe<Current> tryCurrent(Place place, String language, String units);

    Maybe<List<Hourly>> tryHourlies(Place place, String language, String units);
}
