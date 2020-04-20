package ua.in.khol.oleh.touristweathercomparer.model.weather;

/**
 * Created by Oleh Kholiavchuk.
 */

public class WeatherData {

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

    private WeatherData() {

    }

    public boolean getTemp() {
        return mIsCurrent;
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

        private final WeatherData weatherData;

        public Builder() {
            weatherData = new WeatherData();
        }

        public Builder isCurrent(boolean isCurrent) {
            weatherData.mIsCurrent = isCurrent;
            return this;
        }

        public Builder withDate(int date) {
            weatherData.mDate = date;
            return this;
        }

        public Builder withLow(float low) {
            weatherData.mLow = low;
            return this;
        }

        public Builder withHigh(float high) {
            weatherData.mHigh = high;
            return this;
        }

        public Builder withText(String text) {
            weatherData.mText = text;
            return this;
        }

        public Builder withSrc(String src) {
            weatherData.mSrc = src;
            return this;
        }

        public WeatherData build() {
            return weatherData;
        }

        public Builder withPressure(float pressure) {
            weatherData.mPressure = pressure;
            return this;
        }

        public Builder withSpeed(float speed) {
            weatherData.mSpeed = speed;
            return this;
        }

        public Builder withDegree(int degree) {
            weatherData.mDegree = degree;
            return this;
        }

        public Builder withHumidity(int humidity) {
            weatherData.mHumidity = humidity;
            return this;
        }

        public Builder withCurrent(boolean isCurrent) {
            weatherData.mIsCurrent = isCurrent;
            return this;
        }
    }
}
