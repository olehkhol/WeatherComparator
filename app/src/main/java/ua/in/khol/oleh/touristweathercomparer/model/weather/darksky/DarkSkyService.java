package ua.in.khol.oleh.touristweathercomparer.model.weather.darksky;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.DarkSkyData;

public interface DarkSkyService {

    @GET("/forecast/{key}/{latitude},{longitude}")
    Observable<DarkSkyData> getLocationWeather(@Path("key") String key,
                                               @Path("latitude") String latitude,
                                               @Path("longitude") String longitude,
                                               @Query("exclude") String exclude,
                                               @Query("lang") String lang,
                                               @Query("units") String units);
}
