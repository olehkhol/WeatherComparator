package ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    @Expose
    public float temp;
    @SerializedName("feels_like")
    @Expose
    public float feelsLike;
    @SerializedName("temp_min")
    @Expose
    public float tempMin;
    @SerializedName("temp_max")
    @Expose
    public float tempMax;
    @SerializedName("pressure")
    @Expose
    public int pressure;
    @SerializedName("humidity")
    @Expose
    public int humidity;
    @SerializedName("sea_level")
    @Expose
    public int seaLevel;
    @SerializedName("grnd_level")
    @Expose
    public int grndLevel;
    @SerializedName("temp_kf")
    @Expose
    public float tempKf;
}
