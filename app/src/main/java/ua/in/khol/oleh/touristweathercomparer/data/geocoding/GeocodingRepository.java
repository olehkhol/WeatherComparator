package ua.in.khol.oleh.touristweathercomparer.data.geocoding;

import io.reactivex.Maybe;

public interface GeocodingRepository {

    Maybe<String> tryLocationName(double latitude, double longitude, String language);
}