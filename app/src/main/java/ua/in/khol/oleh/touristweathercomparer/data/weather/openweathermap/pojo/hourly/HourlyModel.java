
package ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.hourly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HourlyModel {

    @SerializedName("cod")
    @Expose
    public String cod;
    @SerializedName("message")
    @Expose
    public int message;
    @SerializedName("cnt")
    @Expose
    public int cnt;
    @SerializedName("list")
    @Expose
    public List<Three> threes;
    @SerializedName("city")
    @Expose
    public City city;
}
