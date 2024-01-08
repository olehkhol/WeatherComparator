
package ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.hourly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Clouds;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Main;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Sys;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Weather;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Wind;

public class Three {

    @SerializedName("dt")
    @Expose
    public int dt;
    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("weather")
    @Expose
    public List<Weather> weather;
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("visibility")
    @Expose
    public int visibility;
    @SerializedName("pop")
    @Expose
    public float pop;
    @SerializedName("sys")
    @Expose
    public Sys sys;
    @SerializedName("dt_txt")
    @Expose
    public String dtTxt;
    @SerializedName("rain")
    @Expose
    public Rain rain;
}
