package ua.in.khol.oleh.touristweathercomparer.model.weather.darksky;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.Currently;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.DarkSkyData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.Datum;

public class DarkSky extends WeatherProvider {
    private DarkSkyService mService;

    public DarkSky() {
        super("DarkSky", "https://darksky.net/poweredby/",
                "https://api.darksky.net");
        mService = createService(DarkSkyService.class);
    }

    @Override
    public List<WeatherData> getWeatherDataList(double latitude, double longitude) {
        List<WeatherData> weatherDataList = new ArrayList<>();

        try {
            DarkSkyData darkSkyData = mService.getLocationWeather(DarkSkyAuth.getSecretKey(),
                    String.valueOf(latitude), String.valueOf(longitude), "minutely,hourly,flags",
                    "en", "us").execute().body();

            if (darkSkyData != null) {
                List<Datum> datumList;
                Currently currently;
                datumList = darkSkyData.getDaily().getData();
                currently = darkSkyData.getCurrently();

                int count = 0;
                for (Datum datum : datumList)
                    if (count < DAYS) {
                        WeatherData.Builder builder = new WeatherData.Builder();
                        builder
                                .withProviderId(getId())
                                .withDate(datum.getTime())
                                .withLow(datum.getTemperatureMin())
                                .withHigh(datum.getTemperatureMax())
                                .withText(datum.getIcon())
                                .withSrc(PATH + datum.getIcon());

                        if (count == 0) { // Extra weather data
                            builder
                                    .isCurrent(true)
                                    .withCurrent(currently.getTemperature())
                                    .withWind(String.valueOf(currently.getWindSpeed()))
                                    .withHumidity(String
                                            .valueOf((int) (currently.getHumidity() * 100)))
                                    .withTextExtra(currently.getIcon())
                                    .withSrcExtra(PATH + currently.getIcon());
                        }

                        weatherDataList.add(builder.build());
                        count++;
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherDataList;
    }

    @Override
    public Observable<WeatherData> observeWeatherData(double latitude,
                                                      double longitude) {
        Observable<DarkSkyData> observable = mService.observeLocationWeather(DarkSkyAuth.getSecretKey(),
                String.valueOf(latitude), String.valueOf(longitude), "minutely,hourly,flags",
                "en", "us");

        return observable
                .flatMap(new Function<DarkSkyData, Observable<WeatherData>>() {
                    @Override
                    public Observable<WeatherData> apply(DarkSkyData darkSkyData) throws Exception {
                        List<Datum> datumList;
                        Currently currently;

                        datumList = darkSkyData.getDaily().getData();
                        currently = darkSkyData.getCurrently();

                        List<WeatherData> weatherDataList = new ArrayList<>();
                        int count = 0;
                        for (Datum datum : datumList) {
                            if (count < DAYS) {
                                WeatherData.Builder builder = new WeatherData.Builder();
                                builder
                                        .withProviderId(getId())
                                        .withDate(datum.getTime())
                                        .withLow(datum.getTemperatureMin())
                                        .withHigh(datum.getTemperatureMax())
                                        .withText(datum.getIcon())
                                        .withSrc(PATH + datum.getIcon());

                                if (count == 0) { // Extra weather data
                                    builder
                                            .isCurrent(true)
                                            .withCurrent(currently.getTemperature())
                                            .withWind(String.valueOf(currently.getWindSpeed()))
                                            .withHumidity(String
                                                    .valueOf((int) (currently.getHumidity() * 100)))
                                            .withTextExtra(currently.getIcon())
                                            .withSrcExtra(PATH + currently.getIcon());
                                }

                                weatherDataList.add(builder.build());
                                count++;
                            }
                        }

                        return Observable.fromIterable(weatherDataList);
                    }
                });
    }
}
