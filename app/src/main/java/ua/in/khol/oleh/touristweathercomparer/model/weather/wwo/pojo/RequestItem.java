package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo;

import com.google.gson.annotations.SerializedName;

public class RequestItem {

    @SerializedName("query")
    private String query;

    @SerializedName("type")
    private String type;

    public String getQuery() {
        return query;
    }

    public String getType() {
        return type;
    }
}