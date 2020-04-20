package ua.in.khol.oleh.touristweathercomparer.model.db.data;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;

@Entity(indices = {@Index(value = {"latitude", "longitude", "language"}, unique = true)})
public class Place {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "latitude")
    private double mLatitude;

    @ColumnInfo(name = "longitude")
    private double mLongitude;

    @ColumnInfo(name = "language")
    private String mLanguage;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "offset")
    private int mOffset;

    public Place() {
    }

    @Ignore
    public Place(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    @Ignore
    public Place(LatLon latLon) {
        mLatitude = latLon.getLat();
        mLongitude = latLon.getLon();
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
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

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        mOffset = offset;
    }

    // TODO write tests for equals and hashCode

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Place) {
            Place place = (Place) obj;

            return mLatitude == place.getLatitude() && mLongitude == place.getLongitude()
                    && mLanguage.equals(place.getLanguage());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = Double.valueOf(mLatitude).hashCode();
        result = 31 * result + Double.valueOf(mLongitude).hashCode();
        result = 31 * result + (mLanguage == null ? 0 : mLanguage.hashCode());

        return result;
    }
}
