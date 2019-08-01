package ua.in.khol.oleh.touristweathercomparer.model.weather;

import androidx.annotation.Nullable;

import java.util.List;

public class ProviderData {
    private String mName;
    private String mUrl;
    private String mBanner;
    private List<WeatherData> mWeatherDataList;

    public ProviderData(String name, String url, String banner, List<WeatherData> weatherDataList) {
        mName = name;
        mUrl = url;
        mBanner = banner;
        mWeatherDataList = weatherDataList;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getBanner() {
        return mBanner;
    }

    public List<WeatherData> getWeatherDataList() {
        return mWeatherDataList;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.mName.equals(((ProviderData) obj).mName);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
