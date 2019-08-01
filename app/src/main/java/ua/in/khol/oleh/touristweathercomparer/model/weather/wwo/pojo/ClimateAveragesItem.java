package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClimateAveragesItem {

    @SerializedName("month")
    private List<MonthItem> month;

    public List<MonthItem> getMonth() {
        return month;
    }
}