package ua.in.khol.oleh.touristweathercomparer.data.maps;

import io.reactivex.Maybe;

public interface MapsRepository {

    Maybe<String> getLocationName(double lat, double lon, String language);
}