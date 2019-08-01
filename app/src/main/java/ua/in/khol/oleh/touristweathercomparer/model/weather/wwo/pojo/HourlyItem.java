package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HourlyItem {

    @SerializedName("weatherCode")
    private String weatherCode;

    @SerializedName("chanceofhightemp")
    private String chanceofhightemp;

    @SerializedName("FeelsLikeF")
    private String feelsLikeF;

    @SerializedName("cloudcover")
    private String cloudcover;

    @SerializedName("WindChillC")
    private String windChillC;

    @SerializedName("windspeedMiles")
    private String windspeedMiles;

    @SerializedName("winddirDegree")
    private String winddirDegree;

    @SerializedName("DewPointC")
    private String dewPointC;

    @SerializedName("chanceofthunder")
    private String chanceofthunder;

    @SerializedName("chanceoffrost")
    private String chanceoffrost;

    @SerializedName("DewPointF")
    private String dewPointF;

    @SerializedName("humidity")
    private String humidity;

    @SerializedName("winddir16Point")
    private String winddir16Point;

    @SerializedName("WindChillF")
    private String windChillF;

    @SerializedName("weatherIconUrl")
    private List<WeatherIconUrlItem> weatherIconUrl;

    @SerializedName("tempF")
    private String tempF;

    @SerializedName("precipMM")
    private String precipMM;

    @SerializedName("weatherDesc")
    private List<WeatherDescItem> weatherDesc;

    @SerializedName("chanceofrain")
    private String chanceofrain;

    @SerializedName("chanceofovercast")
    private String chanceofovercast;

    @SerializedName("visibility")
    private String visibility;

    @SerializedName("pressure")
    private String pressure;

    @SerializedName("chanceofsunshine")
    private String chanceofsunshine;

    @SerializedName("WindGustMiles")
    private String windGustMiles;

    @SerializedName("chanceofsnow")
    private String chanceofsnow;

    @SerializedName("FeelsLikeC")
    private String feelsLikeC;

    @SerializedName("windspeedKmph")
    private String windspeedKmph;

    @SerializedName("WindGustKmph")
    private String windGustKmph;

    @SerializedName("chanceoffog")
    private String chanceoffog;

    @SerializedName("HeatIndexC")
    private String heatIndexC;

    @SerializedName("time")
    private String time;

    @SerializedName("chanceofwindy")
    private String chanceofwindy;

    @SerializedName("tempC")
    private String tempC;

    @SerializedName("HeatIndexF")
    private String heatIndexF;

    @SerializedName("chanceofremdry")
    private String chanceofremdry;

    public String getWeatherCode() {
        return weatherCode;
    }

    public String getChanceofhightemp() {
        return chanceofhightemp;
    }

    public String getFeelsLikeF() {
        return feelsLikeF;
    }

    public String getCloudcover() {
        return cloudcover;
    }

    public String getWindChillC() {
        return windChillC;
    }

    public String getWindspeedMiles() {
        return windspeedMiles;
    }

    public String getWinddirDegree() {
        return winddirDegree;
    }

    public String getDewPointC() {
        return dewPointC;
    }

    public String getChanceofthunder() {
        return chanceofthunder;
    }

    public String getChanceoffrost() {
        return chanceoffrost;
    }

    public String getDewPointF() {
        return dewPointF;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWinddir16Point() {
        return winddir16Point;
    }

    public String getWindChillF() {
        return windChillF;
    }

    public List<WeatherIconUrlItem> getWeatherIconUrl() {
        return weatherIconUrl;
    }

    public String getTempF() {
        return tempF;
    }

    public String getPrecipMM() {
        return precipMM;
    }

    public List<WeatherDescItem> getWeatherDesc() {
        return weatherDesc;
    }

    public String getChanceofrain() {
        return chanceofrain;
    }

    public String getChanceofovercast() {
        return chanceofovercast;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public String getChanceofsunshine() {
        return chanceofsunshine;
    }

    public String getWindGustMiles() {
        return windGustMiles;
    }

    public String getChanceofsnow() {
        return chanceofsnow;
    }

    public String getFeelsLikeC() {
        return feelsLikeC;
    }

    public String getWindspeedKmph() {
        return windspeedKmph;
    }

    public String getWindGustKmph() {
        return windGustKmph;
    }

    public String getChanceoffog() {
        return chanceoffog;
    }

    public String getHeatIndexC() {
        return heatIndexC;
    }

    public String getTime() {
        return time;
    }

    public String getChanceofwindy() {
        return chanceofwindy;
    }

    public String getTempC() {
        return tempC;
    }

    public String getHeatIndexF() {
        return heatIndexF;
    }

    public String getChanceofremdry() {
        return chanceofremdry;
    }
}