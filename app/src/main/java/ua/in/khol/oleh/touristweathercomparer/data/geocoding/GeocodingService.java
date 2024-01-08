package ua.in.khol.oleh.touristweathercomparer.data.geocoding;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.in.khol.oleh.touristweathercomparer.data.geocoding.pojo.GeocodingModel;

public interface GeocodingService {


    @GET("/maps/api/geocode/json")
    Maybe<GeocodingModel> tryLocationModel(@Query("latlng") String latlng,
                                           @Query("language") String language,
                                           @Query("key") String key);
}