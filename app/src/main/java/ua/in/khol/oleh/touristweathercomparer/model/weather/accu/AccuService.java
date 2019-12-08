package ua.in.khol.oleh.touristweathercomparer.model.weather.accu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ua.in.khol.oleh.touristweathercomparer.model.weather.accu.pojo.forecast.AccuForecastData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.accu.pojo.forecast.hourly.AccuHourlyForecastData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.accu.pojo.location.AccuLocationData;

public interface AccuService {

    @GET("/locations/v1/cities/geoposition/search")
    Call<AccuLocationData> getLocationKey(@Query("apikey") String apikey,
                                          @Query("q") String q,
                                          @Query("language") String language);

    @GET("/forecasts/v1/daily/5day/{locationKey}")
    Call<AccuForecastData> getForecast(@Path("locationKey") String locationKey,
                                       @Query("apikey") String apikey,
                                       @Query("language") String language);

    @GET("/forecasts/v1/hourly/1hour/{locationKey}")
    Call<List<AccuHourlyForecastData>> getHorlyForecast(@Path("locationKey") String locationKey,
                                                        @Query("apikey") String apikey,
                                                        @Query("language") String language);
}
