package ua.in.khol.oleh.touristweathercomparer.model.db.data;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"provider_id", "place_id", "date"}, unique = true)})
public class Daily {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "provider_id")
    private long mProviderId;

    @ColumnInfo(name = "place_id")
    private long mPlaceId;

    @ColumnInfo(name = "date")
    private int mDate;

    @ColumnInfo(name = "low")
    private float mLow;

    @ColumnInfo(name = "high")
    private float mHigh;

    @ColumnInfo(name = "pressure")
    private float mPressure;

    @ColumnInfo(name = "speed")
    private float mSpeed;

    @ColumnInfo(name = "degree")
    private int mDegree;

    @ColumnInfo(name = "humidity")
    private int mHumidity;

    @ColumnInfo(name = "text")
    private String mText;

    @ColumnInfo(name = "image")
    private String mImage;

    public Daily() {
    }

    @Ignore
    public Daily(int providerId, long placeId) {
        mProviderId = providerId;
        mPlaceId = placeId;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getProviderId() {
        return mProviderId;
    }

    public void setProviderId(long providerId) {
        mProviderId = providerId;
    }

    public long getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(long placeId) {
        mPlaceId = placeId;
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

    public float getPressure() {
        return mPressure;
    }

    public void setPressure(float pressure) {
        mPressure = pressure;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    public int getDegree() {
        return mDegree;
    }

    public void setDegree(int degree) {
        mDegree = degree;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    @Override
    public int hashCode() {
        int result = Long.valueOf(mProviderId).hashCode();
        result = 31 * result + Long.valueOf(mPlaceId).hashCode();
        result = 31 * result + Integer.valueOf(mDate).hashCode();

        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof Daily) {
            Daily daily = (Daily) obj;

            return mProviderId == daily.getProviderId()
                    && mPlaceId == daily.getPlaceId()
                    && mDate == daily.getDate();
        }

        return false;
    }
}
