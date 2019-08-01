package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentConditionItem {

    @SerializedName("precipMM")
    private String precipMM;

    @SerializedName("observation_time")
    private String observationTime;

    @SerializedName("weatherDesc")
    private List<WeatherDescItem> weatherDesc;

    @SerializedName("visibility")
    private String visibility;

    @SerializedName("weatherCode")
    private String weatherCode;

    @SerializedName("FeelsLikeF")
    private String feelsLikeF;

    @SerializedName("pressure")
    private String pressure;

    @SerializedName("temp_C")
    private String tempC;

    @SerializedName("temp_F")
    private String tempF;

    @SerializedName("cloudcover")
    private String cloudcover;

    @SerializedName("windspeedMiles")
    private String windspeedMiles;

    @SerializedName("winddirDegree")
    private String winddirDegree;

    @SerializedName("FeelsLikeC")
    private String feelsLikeC;

    @SerializedName("windspeedKmph")
    private String windspeedKmph;

    @SerializedName("humidity")
    private String humidity;

    @SerializedName("winddir16Point")
    private String winddir16Point;

    @SerializedName("weatherIconUrl")
    private List<WeatherIconUrlItem> weatherIconUrl;

    public String getPrecipMM() {
        return precipMM;
    }

    public String getObservationTime() {
        return observationTime;
    }

    public List<WeatherDescItem> getWeatherDesc() {
        return weatherDesc;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public String getFeelsLikeF() {
        return feelsLikeF;
    }

    public String getPressure() {
        return pressure;
    }

    public String getTempC() {
        return tempC;
    }

    public String getTempF() {
        return tempF;
    }

    public String getCloudcover() {
        return cloudcover;
    }

    public String getWindspeedMiles() {
        return windspeedMiles;
    }

    public String getWinddirDegree() {
        return winddirDegree;
    }

    public String getFeelsLikeC() {
        return feelsLikeC;
    }

    public String getWindspeedKmph() {
        return windspeedKmph;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWinddir16Point() {
        return winddir16Point;
    }

    public List<WeatherIconUrlItem> getWeatherIconUrl() {
        return weatherIconUrl;
    }
}