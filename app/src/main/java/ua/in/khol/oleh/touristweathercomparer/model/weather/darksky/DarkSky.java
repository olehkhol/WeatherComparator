package ua.in.khol.oleh.touristweathercomparer.model.weather.darksky;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import ua.in.khol.oleh.touristweathercomparer.model.weather.AbstractProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.Currently;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.DarkSkyData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.Datum;

public class DarkSky extends AbstractProvider {
    private DarkSkyService mService;

    public DarkSky() {
        super("DarkSky", "https://darksky.net/poweredby/",
                "https://api.darksky.net");
        mService = createService(DarkSkyService.class);
    }


    @Override
    public Observable<ProviderData> getWeatherObservable(double latitude,
                                                         double longitude) {
        String unit = "us";
        String exclude = "minutely,hourly,flags";
        String language = getLanguage();

        Observable<DarkSkyData> observable = mService
                .getLocationWeather(DarkSkyAuth.getSecretKey(),
                        String.valueOf(latitude), String.valueOf(longitude),
                        exclude, language, unit);

        return observable
                .map(darkSkyData -> {
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
                                    .withDate(datum.getTime())
                                    .withLow(datum.getTemperatureMin())
                                    .withHigh(datum.getTemperatureMax())
                                    .withText(datum.getIcon())
                                    .withSrc(PATH + datum.getIcon());

                            if (count == 0) { // Extra weather data
                                builder
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

                    return DarkSky.this.compositeProviderData(weatherDataList);
                });
    }

}
