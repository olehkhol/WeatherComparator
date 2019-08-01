package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo;

import com.google.gson.annotations.SerializedName;

public class WeatherIconUrlItem {

    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }
}