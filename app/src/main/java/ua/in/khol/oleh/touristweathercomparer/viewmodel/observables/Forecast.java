package ua.in.khol.oleh.touristweathercomparer.viewmodel.observables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"provider_id", "location_id", "date"}, unique = true)})
public class Forecast {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "location_id")
    private long mLocationId;

    @ColumnInfo(name = "provider_id")
    private long mProviderId;

    @ColumnInfo(name = "date")
    private int mDate;

    @ColumnInfo(name = "low")
    private float mLow;

    @ColumnInfo(name = "high")
    private float mHigh;

    @ColumnInfo(name = "text")
    private String mText;

    @ColumnInfo(name = "src")
    private String mSrc;

    @ColumnInfo(name = "humidity")
    private String mHumidity;

    @Ignore
    public Forecast() {
    }

    public Forecast(long locationId, long providerId,
                    int date, float low, float high, String text, String src, String humidity) {
        mLocationId = locationId;
        mProviderId = providerId;

        mDate = date;
        mLow = low;
        mHigh = high;
        mText = text;
        mSrc = src;
        mHumidity = humidity;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getLocationId() {
        return mLocationId;
    }

    public void setLocationId(long locationId) {
        mLocationId = locationId;
    }

    public long getProviderId() {
        return mProviderId;
    }

    public void setProviderId(long providerId) {
        mProviderId = providerId;
    }

    public int getDate() {
        return mDate;
    }

    public void setDate(int date) {
        mDate = date;
    }

    public float getLow() {
        return mLow;
    }

    public void setLow(float low) {
        mLow = low;
    }

    public float getHigh() {
        return mHigh;
    }

    public void setHigh(float high) {
        mHigh = high;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getSrc() {
        return mSrc;
    }

    public void setSrc(String src) {
        mSrc = src;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = humidity;
    }
}
