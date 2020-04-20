package ua.in.khol.oleh.touristweathercomparer.model.weather.darksky;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.currently.DarkSkyCurrently;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.daily.DarkSkyDaily;

interface DarkSkyService {

    @GET("/forecast/{key}/{latitude},{longitude}?exclude=minutely,hourly,daily,alerts,flags")
    Call<DarkSkyCurrently> getCurrently(@Path("key") String key,
                                        @Path("latitude") String latitude,
                                        @Path("longitude") String longitude,
                                        @Query("lang") String lang,
                                        @Query("units") String units);

    @GET("/forecast/{key}/{latitude},{longitude}?exclude=currently,minutely,hourly,alerts,flags")
    Call<DarkSkyDaily> getDaily(@Path("key") String key,
                                @Path("latitude") String latitude,
                                @Path("longitude") String longitude,
                                @Query("lang") String lang,
                                @Query("units") String units);

    @GET("/forecast/{key}/{latitude},{longitude}?exclude=currently,minutely,hourly,alerts,flags")
    Call<DarkSkyDaily> getDaily(@Path("key") String key,
                                @Path("latitude") String latitude,
                                @Path("longitude") String longitude,
                                @Query("lang") String lang,
                                @Query("units") String units,
                                @Query("time") String time);
}
