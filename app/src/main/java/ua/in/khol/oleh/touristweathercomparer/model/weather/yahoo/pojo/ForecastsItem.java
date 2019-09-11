package ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForecastsItem {

    @SerializedName("date")
    @Expose
    private int date;

    @SerializedName("high")
    @Expose
    private int high;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("low")
    @Expose
    private int low;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("day")
    @Expose
    private String day;

    public void setDate(int date) {
        this.date = date;
    }

    public int getDate() {
        return date;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getHigh() {
        return high;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getLow() {
        return low;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }
}