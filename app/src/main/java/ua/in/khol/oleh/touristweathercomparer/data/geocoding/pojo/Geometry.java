package ua.in.khol.oleh.touristweathercomparer.data.geocoding.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("bounds")
    @Expose
    private Bounds bounds;

    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("location_type")
    @Expose
    private String locationType;

    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    public Bounds getBounds() {
        return bounds;
    }

    public Location getLocation() {
        return location;
    }

    public String getLocationType() {
        return locationType;
    }

    public Viewport getViewport() {
        return viewport;
    }
}