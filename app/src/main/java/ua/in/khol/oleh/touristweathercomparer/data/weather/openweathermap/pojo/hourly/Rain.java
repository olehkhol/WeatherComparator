
package ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.hourly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("3h")
    @Expose
    public float millimeters;
}
