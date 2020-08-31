package ua.in.khol.oleh.touristweathercomparer.model.maps;

import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

public interface MapsHelper {
    int getTimeZoneOffset(double latitude, double longitude, long timestamp);

    String getLocationName(double lat, double lon, String language);

    Maybe<Place> tryPlace(double latitude, double longitude, String language);
}
