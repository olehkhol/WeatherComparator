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
    private float mCurrent;
    private String mWind;
    private String mHumidity;
    private String mTextExtra;
    private String mSrcExtra;

    private WeatherData() {

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

    public float getCurrent() {
        return mCurrent;
    }

    public String getTextExtra() {
        return mTextExtra;
    }

    public String getSrcExtra() {
        return mSrcExtra;
    }

    public String getWind() {
        return mWind;
    }

    public String getHumidity() {
        return mHumidity + " %";
    }

    public static class Builder {

        private WeatherData weatherData;

        public Builder() {
            weatherData = new WeatherData();
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

        public Builder withCurrent(float current) {
            weatherData.mCurrent = current;
            return this;
        }

        public Builder withWind(String wind) {
            weatherData.mWind = wind;
            return this;
        }

        public Builder withHumidity(String humidity) {
            weatherData.mHumidity = humidity;
            return this;
        }

        public Builder withTextExtra(String textExtra) {
            weatherData.mTextExtra = textExtra;
            return this;
        }

        public Builder withSrcExtra(String srcExtra) {
            weatherData.mSrcExtra = srcExtra;
            return this;
        }

        public WeatherData build() {
            return weatherData;
        }

    }
}
