package ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo;

import java.io.IOException;
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
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.pojo.YahooModel;

public class Yahoo extends AbstractProvider {
    private final OkHttpClient mOkHttpClient;

    public Yahoo() {
        super("Yahoo", "http://weather.yahoo.com/",
                "https://weather-ydn-yql.media.yahoo.com");
        YahooInterceptor interceptor = new YahooInterceptor.Builder()
                .appId(YahooAuth.getAppId())
                .consumerKey(YahooAuth.getConsumerKey())
                .consumerSecret(YahooAuth.getConsumerSecret())
                .build();
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(null)
                .build();
    }

    @Override
    public Observable<ProviderData> observeProviderData(double latitude,
                                                        double longitude) {

        YahooService service = new Retrofit.Builder()
                .baseUrl(API)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(YahooService.class);

        return service
                .observeYahooModel(String.valueOf(latitude), String.valueOf(longitude), "json", "f")
                .map(yahooModel -> getProviderData(yahooModel));
    }

    @Override
    public ProviderData getProviderData(double latitude, double longitude) {

        YahooService service = new Retrofit.Builder()
                .baseUrl(API)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(YahooService.class);

        try {
            YahooModel yahooModel = service
                    .getYahooModel(String.valueOf(latitude), String.valueOf(longitude), "json", "f").execute().body();

            return getProviderData(yahooModel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ProviderData getProviderData(YahooModel yahooModel) {
        List<WeatherData> weatherDataList = new ArrayList<>();

        int count = 0;
        for (ForecastsItem forecast : yahooModel.getForecasts()) {
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
                            = yahooModel.getCurrentObservation();
                    if (currentObservation != null) {
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
                }

                weatherDataList.add(builder.build());
                count++;
            }
        }

        return compositeProviderData(weatherDataList);
    }

}
