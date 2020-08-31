package ua.in.khol.oleh.touristweathercomparer.model.weather;

/**
 * Created by Oleh Kholiavchuk.
 */

public class Forecast {

    private int mDate;
    private float mLow;
    private float mHigh;
    private String mText;
    private String mSrc;
    private float mPressure;
    private float mSpeed;
    private int mDegree;
    private int mHumidity;
    private boolean mIsCurrent;

    private Forecast() {

    }

    public int getDate() {
        return mDate;
    }

    public float getLow() {
        return mLow;
    }

    public float getHigh() {
        return mHigh;
    }

    public String getText() {
        return mText;
    }

    public String getSrc() {
        return mSrc;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public float getPressure() {
        return mPressure;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public int getDegree() {
        return mDegree;
    }

    public boolean isCurrent() {
        return mIsCurrent;
    }

    public static class Builder {

        private final Forecast mForecast;

        public Builder() {
            mForecast = new Forecast();
        }

        public Builder isCurrent(boolean isCurrent) {
            mForecast.mIsCurrent = isCurrent;
            return this;
        }

        public Builder withDate(int date) {
            mForecast.mDate = date;
            return this;
        }

        public Builder withLow(float low) {
            mForecast.mLow = low;
            return this;
        }

        public Builder withHigh(float high) {
            mForecast.mHigh = high;
            return this;
        }

        public Builder withText(String text) {
            mForecast.mText = text;
            return this;
        }

        public Builder withSrc(String src) {
            mForecast.mSrc = src;
            return this;
        }

        public Forecast build() {
            return mForecast;
        }

        public Builder withPressure(float pressure) {
            mForecast.mPressure = pressure;
            return this;
        }

        public Builder withSpeed(float speed) {
            mForecast.mSpeed = speed;
            return this;
        }

        public Builder withDegree(int degree) {
            mForecast.mDegree = degree;
            return this;
        }

        public Builder withHumidity(int humidity) {
            mForecast.mHumidity = humidity;
            return this;
        }

        public Builder withCurrent(boolean isCurrent) {
            mForecast.mIsCurrent = isCurrent;
            return this;
        }
    }
}
