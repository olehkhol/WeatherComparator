package ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo;

import com.google.gson.annotations.SerializedName;

public class CurrentObservation {

    @SerializedName("atmosphere")
    private Atmosphere atmosphere;

    @SerializedName("condition")
    private Condition condition;

    @SerializedName("astronomy")
    private Astronomy astronomy;

    @SerializedName("pubDate")
    private int pubDate;

    @SerializedName("wind")
    private Wind wind;

    public void setAtmosphere(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setAstronomy(Astronomy astronomy) {
        this.astronomy = astronomy;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public void setPubDate(int pubDate) {
        this.pubDate = pubDate;
    }

    public int getPubDate() {
        return pubDate;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Wind getWind() {
        return wind;
    }
}