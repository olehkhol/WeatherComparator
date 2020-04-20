package ua.in.khol.oleh.touristweathercomparer.model.maps;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.in.khol.oleh.touristweathercomparer.model.maps.pojo.GeocodingModel;
import ua.in.khol.oleh.touristweathercomparer.model.maps.pojo.TimeZoneModel;


/**
 * Created by Oleh Kholiavchuk.
 */

interface MapsService {

    @GET("/maps/api/timezone/json")
    Call<TimeZoneModel> getTimeZoneModel(@Query("location") String location,
                                         @Query("timestamp") String timestamp,
                                         @Query("key") String key);

    @GET("/maps/api/geocode/json")
    Call<GeocodingModel> getLocationModel(@Query("latlng") String latlng,
                                          @Query("language") String language,
                                          @Query("key") String key);
}
