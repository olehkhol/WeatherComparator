package ua.in.khol.oleh.touristweathercomparer.viewmodel.observables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"name", "place_id"}, unique = true)})
public class City {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "place_id")
    private long mPlaceId;

    @Ignore
    public City() {
        mName = "";
    }

    @Ignore
    public City(String name) {
        mName = name;
    }

    public City(String name, long placeId) {
        mName = name;
        mPlaceId = placeId;
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

    public long getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(long placeId) {
        mPlaceId = placeId;
    }
}
