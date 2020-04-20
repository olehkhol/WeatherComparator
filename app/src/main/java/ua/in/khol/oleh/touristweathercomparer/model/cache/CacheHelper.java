package ua.in.khol.oleh.touristweathercomparer.model.cache;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.model.db.data.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;

public interface CacheHelper {

    void putLatLon(LatLon latLon);

    LatLon getLatLon();

    void putPlace(Place place);

    Place getPlace(double lat, double lon, String lang);

    List<Forecast> getCurrents(long placeId, int date);

    void putCurrents(long placeId, List<Forecast> forecasts);

    List<Forecast> getDailies(long placeId, int date);

    void putDailies(long placeId, List<Forecast> forecastList);

}
