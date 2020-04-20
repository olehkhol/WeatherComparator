package ua.in.khol.oleh.touristweathercomparer.model.maps;

public interface MapsHelper {
    int getTimeZoneOffset(double latitude, double longitude, long timestamp);

    String getLocationName(double lat, double lon, String language);
}
