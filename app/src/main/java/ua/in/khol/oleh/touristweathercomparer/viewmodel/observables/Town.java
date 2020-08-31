package ua.in.khol.oleh.touristweathercomparer.viewmodel.observables;

public class Town {

    private String mName;
    private long mPlaceId;
    private double mLatitude;
    private double mLongitude;

    public Town(String name, long placeId) {
        mName = name;
        mPlaceId = placeId;
    }

    public Town(String name, long placeId, double latitude, double longitude) {
        this(name, placeId);
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(long placeId) {
        mPlaceId = placeId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
