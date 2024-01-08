package ua.in.khol.oleh.touristweathercomparer.data.geocoding.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("address_components")
    @Expose
    public List<AddressComponent> addressComponents = null;

    @SerializedName("formatted_address")
    @Expose
    public String formattedAddress;

    @SerializedName("geometry")
    @Expose
    public Geometry geometry;

    @SerializedName("place_id")
    @Expose
    public String placeId;

    @SerializedName("plus_code")
    @Expose
    public PlusCode plusCode;

    @SerializedName("types")
    @Expose
    public List<String> types = null;
}