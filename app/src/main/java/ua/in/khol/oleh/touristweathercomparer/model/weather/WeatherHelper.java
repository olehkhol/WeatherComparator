package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Current;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Daily;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

public interface WeatherHelper {

    List<Current> getCurrents(Place place, int time);

    List<Daily> getDailies(Place place, int date);

    Maybe<List<Current>> tryCurrents(Place place, int date);

    MaybeSource<List<Daily>> tryDailies(Place place, int date);
}