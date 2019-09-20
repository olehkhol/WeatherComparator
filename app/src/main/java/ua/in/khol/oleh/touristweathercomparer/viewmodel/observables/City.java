package ua.in.khol.oleh.touristweathercomparer.viewmodel.observables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"latitude", "longitude"}, unique = true)})
public class City {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "latitude")
    private double mLatitude;

    @ColumnInfo(name = "longitude")
    private double mLongitude;

    @Ignore
    public City() {
        mName = "";
    }

    @Ignore
    public City(String name) {
        mName = name;
    }

    @Ignore
    public City(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public City(String name, double latitude, double longitude) {
        mName = name;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
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
