package ua.in.khol.oleh.touristweathercomparer.viewmodel.observables;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"provider_id", "place_id", "date"}, unique = true)})
public class Forecast {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "provider_id")
    private int mProviderId;

    @ColumnInfo(name = "place_id")
    private long mPlaceId;

    @ColumnInfo(name = "date")
    private int mDate;

    @ColumnInfo(name = "low")
    private float mLow;

    @ColumnInfo(name = "high")
    private float mHigh;

    @ColumnInfo(name = "text")
    private String mText;

    @ColumnInfo(name = "image")
    private String mImage;

    @ColumnInfo(name = "humidity")
    private String mHumidity;

    @ColumnInfo(name = "is_current")
    private boolean mIsCurrent;

    @ColumnInfo(name = "current")
    private float mCurrent;

    @ColumnInfo(name = "current_text")
    private String mCurrentText;

    @ColumnInfo(name = "current_image")
    private String mCurrentImage;

    @Ignore
    public Forecast() {
    }

    public Forecast(int providerId, int date, float low, float high, String text, String image, String humidity) {
        mProviderId = providerId;
        mDate = date;
        mLow = low;
        mHigh = high;
        mText = text;
        mImage = image;
        mHumidity = humidity;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public int getProviderId() {
        return mProviderId;
    }

    public void setProviderId(int providerId) {
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

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = humidity;
    }

    public boolean isCurrent() {
        return mIsCurrent;
    }

    public void setIsCurrent(boolean current) {
        mIsCurrent = current;
    }

    public float getCurrent() {
        return mCurrent;
    }

    public void setCurrent(float current) {
        mCurrent = current;
    }

    public String getCurrentText() {
        return mCurrentText;
    }

    public void setCurrentText(String currentText) {
        mCurrentText = currentText;
    }

    public String getCurrentImage() {
        return mCurrentImage;
    }

    public void setCurrentImage(String currentImage) {
        mCurrentImage = currentImage;
    }

    // TODO write tests for equals and hashCode

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof Forecast) {
            Forecast forecast = (Forecast) obj;

            return this.mProviderId == forecast.mProviderId
                    && this.mPlaceId == forecast.mPlaceId
                    && this.mDate == forecast.mDate;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = Integer.valueOf(mProviderId).hashCode();
        result = 31 * result + Long.valueOf(mPlaceId).hashCode();
        result = 31 * result + Integer.valueOf(mDate).hashCode();

        return result;
    }
}
