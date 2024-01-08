package ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.current;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Clouds;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Coord;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Main;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Sys;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Weather;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Wind;

public class CurrentModel {

    @SerializedName("coord")
    @Expose
    public Coord coord;
    @SerializedName("weather")
    @Expose
    public List<Weather> weather;
    @SerializedName("base")
    @Expose
    public String base;
    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("visibility")
    @Expose
    public int visibility;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("dt")
    @Expose
    public int dt;
    @SerializedName("sys")
    @Expose
    public Sys sys;
    @SerializedName("timezone")
    @Expose
    public int timezone;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("cod")
    @Expose
    public int cod;
}