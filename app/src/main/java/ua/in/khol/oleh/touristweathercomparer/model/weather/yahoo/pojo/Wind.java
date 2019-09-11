package ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("chill")
    @Expose
    private int chill;

    @SerializedName("speed")
    @Expose
    private double speed;

    @SerializedName("direction")
    @Expose
    private int direction;

    public void setChill(int chill) {
        this.chill = chill;
    }

    public int getChill() {
        return chill;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}