package ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.in.khol.oleh.touristweathercomparer.model.weather.AbstractProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo.Atmosphere;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo.Condition;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo.CurrentObservation;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo.ForecastsItem;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo.Wind;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo.YahooData;

public class Yahoo extends AbstractProvider {
    private YahooService mService;

    public Yahoo() {
        super("Yahoo", "http://weather.yahoo.com/",
                "https://weather-ydn-yql.media.yahoo.com");
        mService = createService(YahooService.class);
    }

    @Override
    protected Retrofit buildRetrofit() {
        YahooInterceptor interceptor = new YahooInterceptor.Builder()
                .appId(YahooAuth.APP_ID)
                .consumerKey(YahooAuth.CONSUMER_KEY)
                .consumerSecret(YahooAuth.CONSUMER_SECRET)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(null)
                .build();
        return new Retrofit.Builder()
                .baseUrl(API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Override
    public Observable<ProviderData> getWeatherObservable(double latitude,
                                                         double longitude) {
        String u = "f";
        String format = "json";

        Observable<YahooData> observable = mService
                .getLocationWeather(String.valueOf(latitude), String.valueOf(longitude),
                        format, u);

        return observable
                .map(yahooData -> {
                    List<WeatherData> weatherDataList = new ArrayList<>();

                    int count = 0;
                    for (ForecastsItem forecast : yahooData.getForecasts()) {
                        if (count < DAYS) {
                            WeatherData.Builder builder = new WeatherData.Builder();
                            builder
                                    .withDate(forecast.getDate())
                                    .withLow(forecast.getLow())
                                    .withHigh(forecast.getHigh())
                                    .withText(forecast.getText())
                                    .withSrc(PATH + forecast.getCode());

                            if (count == 0) {
                                CurrentObservation currentObservation
                                        = yahooData.getCurrentObservation();
                                Condition condition = currentObservation.getCondition();
                                Wind wind = currentObservation.getWind();
                                Atmosphere atmosphere = currentObservation.getAtmosphere();
                                builder
                                        .withCurrent(condition.getTemperature())
                                        .withWind(String.valueOf(wind.getSpeed()))
                                        .withHumidity(String.valueOf(atmosphere.getHumidity()))
                                        .withTextExtra(condition.getText())
                                        .withSrcExtra(PATH + condition.getCode());
                            }

                            weatherDataList.add(builder.build());
                            count++;
                        }
                    }

                    return Yahoo.this.compositeProviderData(weatherDataList);
                });
    }
}
