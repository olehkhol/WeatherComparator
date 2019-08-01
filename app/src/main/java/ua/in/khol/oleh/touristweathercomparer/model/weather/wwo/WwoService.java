package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo.WWOData;

public interface WwoService {

    @GET("premium/v1/weather.ashx")
    Observable<WWOData> getLocationWeather(
            @Query("q") String q,
            @Query("key") String key,
            @Query("lang") String lang,
            @Query("num_of_days") String num_of_days,
            @Query("format") String format,
            @Query("tp") String tp);
}