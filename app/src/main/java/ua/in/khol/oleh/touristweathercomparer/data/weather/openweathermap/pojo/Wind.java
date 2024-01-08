package ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    @Expose
    public float speed;
    @SerializedName("deg")
    @Expose
    public int deg;
    @SerializedName("gust")
    @Expose
    public float gust;
}