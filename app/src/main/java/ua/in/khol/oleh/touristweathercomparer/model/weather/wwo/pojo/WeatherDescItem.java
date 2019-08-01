package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo;

import com.google.gson.annotations.SerializedName;

public class WeatherDescItem {

    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }
}