package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.model.db.data.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

public interface WeatherHelper {

    List<Forecast> getCurrents(Place place, int time);

    List<Forecast> getDailies(Place place, int date);

}