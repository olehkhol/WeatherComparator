package ua.in.khol.oleh.touristweathercomparer.model.db.data;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import ua.in.khol.oleh.touristweathercomparer.model.weather.Forecast;

@Entity(indices = {@Index(value = {"provider_id", "place_id"}, unique = true)})
public class Current {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "provider_id")
    private long mProviderId;

    @ColumnInfo(name = "place_id")
    private long mPlaceId;

    @ColumnInfo(name = "time")
    private int mTime;

    @ColumnInfo(name = "temp")
    private float mTemp;

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

    public Current() {
    }

    @Ignore
    public Current(int providerId, long placeId) {
        mProviderId = providerId;
        mPlaceId = placeId;
    }

    public static Current fromForecast(int providerId, long placeId, Forecast forecast) {
        Current current = new Current(providerId, placeId);

        current.setTime(forecast.getDate());
        current.setTemp((forecast.getLow() + forecast.getHigh()) / 2);
        current.setPressure(forecast.getPressure());
        current.setSpeed(forecast.getSpeed());
        current.setDegree(forecast.getDegree());
        current.setHumidity(forecast.getHumidity());
        current.setText(forecast.getText());
        current.setImage(forecast.getSrc());

        return current;
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

    public int getTime() {
        return mTime;
    }

    public void setTime(int time) {
        mTime = time;
    }

    public float getTemp() {
        return mTemp;
    }

    public void setTemp(float temp) {
        mTemp = temp;
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

        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof Current) {
            Current daily = (Current) obj;

            return mProviderId == daily.getProviderId()
                    && mPlaceId == daily.getPlaceId();
        }

        return false;
    }
}
