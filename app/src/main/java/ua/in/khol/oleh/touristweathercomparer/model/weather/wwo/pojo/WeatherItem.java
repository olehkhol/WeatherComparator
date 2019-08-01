package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherItem {

    @SerializedName("date")
    private String date;

    @SerializedName("sunHour")
    private String sunHour;

    @SerializedName("mintempF")
    private String mintempF;

    @SerializedName("mintempC")
    private String mintempC;

    @SerializedName("totalSnow_cm")
    private String totalSnowCm;

    @SerializedName("maxtempF")
    private String maxtempF;

    @SerializedName("hourly")
    private List<HourlyItem> hourly;

    @SerializedName("astronomy")
    private List<AstronomyItem> astronomy;

    @SerializedName("uvIndex")
    private String uvIndex;

    @SerializedName("maxtempC")
    private String maxtempC;

    public String getDate() {
        return date;
    }

    public String getSunHour() {
        return sunHour;
    }

    public String getMintempF() {
        return mintempF;
    }

    public String getMintempC() {
        return mintempC;
    }

    public String getTotalSnowCm() {
        return totalSnowCm;
    }

    public String getMaxtempF() {
        return maxtempF;
    }

    public List<HourlyItem> getHourly() {
        return hourly;
    }

    public List<AstronomyItem> getAstronomy() {
        return astronomy;
    }

    public String getUvIndex() {
        return uvIndex;
    }

    public String getMaxtempC() {
        return maxtempC;
    }
}