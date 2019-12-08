package ua.in.khol.oleh.touristweathercomparer.model.weather.accu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.accu.pojo.forecast.AccuForecastData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.accu.pojo.forecast.DailyForecast;
import ua.in.khol.oleh.touristweathercomparer.model.weather.accu.pojo.forecast.hourly.AccuHourlyForecastData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.accu.pojo.location.AccuLocationData;

public class Accu extends WeatherProvider {
    public static final String LANGUAGE = "en-us";
    private AccuService mService;

    public Accu() {
        super("Accu", "https://www.accuweather.com/",
                "http://dataservice.accuweather.com");
        mService = createService(AccuService.class);
    }

    @Override
    public List<WeatherData> getWeatherDataList(double latitude, double longitude) {
        List<WeatherData> weatherDataList = new ArrayList<>();

        try {
            AccuLocationData accuLocationData = mService.getLocationKey(AccuAuth.getSecretKey(),
                    latitude + "," + longitude, LANGUAGE).execute().body();
            String locationKey;
            if (accuLocationData != null && (locationKey = accuLocationData.getKey()) != null) {
                AccuForecastData accuForecastData = mService
                        .getForecast(locationKey, AccuAuth.getSecretKey(), LANGUAGE)
                        .execute().body();
                if (accuForecastData != null) {
                    List<DailyForecast> dailyForecasts = accuForecastData.getDailyForecasts();
                    int count = 0;
                    for (DailyForecast dailyForecast : dailyForecasts)
                        if (count < DAYS) {
                            WeatherData.Builder builder = new WeatherData.Builder();
                            builder
                                    .withProviderId(getId())
                                    .withDate(dailyForecast.getEpochDate())
                                    .withLow(dailyForecast.getTemperature().getMinimum().getValue())
                                    .withHigh(dailyForecast.getTemperature().getMaximum().getValue())
                                    .withText(dailyForecast.getDay().getIconPhrase())
                                    .withSrc(PATH + dailyForecast.getDay().getIcon());

                            if (count == 0) {
                                List<AccuHourlyForecastData> accuHourlyForecastDatas = mService
                                        .getHorlyForecast(locationKey, AccuAuth.getSecretKey(),
                                                LANGUAGE).execute().body();
                                if (accuHourlyForecastDatas != null
                                        && accuHourlyForecastDatas.size() > 0) {
                                    builder
                                            .isCurrent(true)
                                            .withCurrent(accuHourlyForecastDatas.get(0).getTemperature().getValue())
                                            .withTextExtra(accuHourlyForecastDatas.get(0).getIconPhrase())
                                            .withSrcExtra(PATH + accuHourlyForecastDatas.get(0).getWeatherIcon());
                                }
                            }
                            weatherDataList.add(builder.build());
                            count++;
                        }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherDataList;
    }

    @Override
    public Observable<WeatherData> observeWeatherData(double latitude, double longitude) {
        return null;
    }
}
