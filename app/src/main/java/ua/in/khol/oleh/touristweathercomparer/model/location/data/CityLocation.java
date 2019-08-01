package ua.in.khol.oleh.touristweathercomparer.model.location.data;

public class CityLocation {
    private double mLatitude;
    private double mLongitude;

    public CityLocation(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
