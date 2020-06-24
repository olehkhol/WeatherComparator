package ua.in.khol.oleh.touristweathercomparer.model.location;

public class LatLon {

    private double mLat;
    private double mLon;

    public LatLon(double lat, double lon) {
        mLat = lat;
        mLon = lon;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        mLon = lon;
    }

    public boolean isValid() {
        if (mLat == 0 && mLon == 0)
            return false;
        return true;
    }
}
