
package ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.hourly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.Coord;

public class City {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("coord")
    @Expose
    public Coord coord;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("population")
    @Expose
    public int population;
    @SerializedName("timezone")
    @Expose
    public int timezone;
    @SerializedName("sunrise")
    @Expose
    public int sunrise;
    @SerializedName("sunset")
    @Expose
    public int sunset;
}
