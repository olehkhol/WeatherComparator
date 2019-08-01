package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("request")
    private List<RequestItem> request;

    @SerializedName("current_condition")
    private List<CurrentConditionItem> currentCondition;

    @SerializedName("weather")
    private List<WeatherItem> weather;

    @SerializedName("ClimateAverages")
    private List<ClimateAveragesItem> climateAverages;

    public List<RequestItem> getRequest() {
        return request;
    }

    public List<CurrentConditionItem> getCurrentCondition() {
        return currentCondition;
    }

    public List<WeatherItem> getWeather() {
        return weather;
    }

    public List<ClimateAveragesItem> getClimateAverages() {
        return climateAverages;
    }
}