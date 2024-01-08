package ua.in.khol.oleh.touristweathercomparer.data.geocoding.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeocodingModel {

    @SerializedName("plus_code")
    @Expose
    private PlusCode plusCode;

    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    @SerializedName("status")
    @Expose
    private String status;

    public PlusCode getPlusCode() {
        return plusCode;
    }

    public List<Result> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }
}