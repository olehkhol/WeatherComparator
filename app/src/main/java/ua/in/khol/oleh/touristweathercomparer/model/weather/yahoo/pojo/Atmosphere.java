package ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Atmosphere {

    @SerializedName("rising")
    @Expose
    private int rising;

    @SerializedName("visibility")
    @Expose
    private double visibility;

    @SerializedName("humidity")
    @Expose
    private int humidity;

    @SerializedName("pressure")
    @Expose
    private double pressure;

    public void setRising(int rising) {
        this.rising = rising;
    }

    public int getRising() {
        return rising;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getPressure() {
        return pressure;
    }
}