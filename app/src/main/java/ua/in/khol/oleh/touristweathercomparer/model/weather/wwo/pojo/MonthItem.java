package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo;

import com.google.gson.annotations.SerializedName;

public class MonthItem {

    @SerializedName("absMaxTemp")
    private String absMaxTemp;

    @SerializedName("avgMinTemp_F")
    private String avgMinTempF;

    @SerializedName("name")
    private String name;

    @SerializedName("index")
    private String index;

    @SerializedName("absMaxTemp_F")
    private String absMaxTempF;

    @SerializedName("avgDailyRainfall")
    private String avgDailyRainfall;

    @SerializedName("avgMinTemp")
    private String avgMinTemp;

    public String getAbsMaxTemp() {
        return absMaxTemp;
    }

    public String getAvgMinTempF() {
        return avgMinTempF;
    }

    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }

    public String getAbsMaxTempF() {
        return absMaxTempF;
    }

    public String getAvgDailyRainfall() {
        return avgDailyRainfall;
    }

    public String getAvgMinTemp() {
        return avgMinTemp;
    }
}