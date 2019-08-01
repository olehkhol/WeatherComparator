package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo;

import com.google.gson.annotations.SerializedName;

public class WWOData {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}