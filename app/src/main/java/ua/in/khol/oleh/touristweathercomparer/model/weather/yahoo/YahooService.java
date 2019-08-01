package ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo.YahooData;

public interface YahooService{

    @GET("/forecastrss")
    Observable<YahooData> getLocationWeather(@Query("lat") String lat,
                                             @Query("lon") String lon,
                                             @Query("format") String format,
                                             @Query("u") String u);
}