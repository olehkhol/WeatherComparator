package ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.current.CurrentModel;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.pojo.hourly.HourlyModel;

public interface OpenWeatherService {

    @GET("/data/2.5/weather")
    Maybe<CurrentModel> tryCurrent(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("lang") String lang,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("/data/2.5/forecast")
    Maybe<HourlyModel> tryHourlies(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("lang") String lang,
            @Query("units") String units,
            @Query("appid") String appid
    );
}