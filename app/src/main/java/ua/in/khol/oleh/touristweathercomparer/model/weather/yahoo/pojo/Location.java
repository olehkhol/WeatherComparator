package ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("woeid")
    @Expose
    private int woeid;

    @SerializedName("timezone_id")
    @Expose
    private String timezoneId;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("long")
    @Expose
    private double jsonMemberLong;

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setWoeid(int woeid) {
        this.woeid = woeid;
    }

    public int getWoeid() {
        return woeid;
    }

    public void setTimezoneId(String timezoneId) {
        this.timezoneId = timezoneId;
    }

    public String getTimezoneId() {
        return timezoneId;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setJsonMemberLong(double jsonMemberLong) {
        this.jsonMemberLong = jsonMemberLong;
    }

    public double getJsonMemberLong() {
        return jsonMemberLong;
    }
}