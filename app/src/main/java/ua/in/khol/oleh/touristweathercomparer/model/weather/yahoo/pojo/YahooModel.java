package ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YahooModel {

    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("current_observation")
    @Expose
    private CurrentObservation currentObservation;

    @SerializedName("forecasts")
    @Expose
    private List<ForecastsItem> forecasts;

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setCurrentObservation(CurrentObservation currentObservation) {
        this.currentObservation = currentObservation;
    }

    public CurrentObservation getCurrentObservation() {
        return currentObservation;
    }

    public void setForecasts(List<ForecastsItem> forecasts) {
        this.forecasts = forecasts;
    }

    public List<ForecastsItem> getForecasts() {
        return forecasts;
    }
}