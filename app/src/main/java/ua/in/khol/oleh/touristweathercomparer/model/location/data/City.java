package ua.in.khol.oleh.touristweathercomparer.model.location.data;

public class City {
    private String mName;
    private double mLatitude;
    private double mLongitude;

    public City(String name, double latitude, double longitude) {
        mName = name;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getName() {
        return mName;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
