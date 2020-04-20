package ua.in.khol.oleh.touristweathercomparer.model.weather.owm;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.pojo.current.OwmCurrentData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.pojo.hourly.OwmHourlyData;

interface OwmService {
    @GET("/data/2.5/forecast")
    Call<OwmHourlyData> getLocationWeather(@Query("lat") String lat,
                                           @Query("lon") String lon,
                                           @Query("lang") String lang,
                                           @Query("units") String units,
                                           @Query("appid") String appid);

    @GET("/data/2.5/weather")
    Call<OwmCurrentData> getCurrentWeather(@Query("lat") String lat,
                                           @Query("lon") String lon,
                                           @Query("lang") String lang,
                                           @Query("units") String units,
                                           @Query("appid") String appid);
}
