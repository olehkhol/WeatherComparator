package ua.in.khol.oleh.touristweathercomparer.model.weather.owm;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.pojo.current.OwmCurrentData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.pojo.hourly.Hourly;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.pojo.hourly.Main;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.pojo.hourly.OwmHourlyData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.pojo.hourly.Weather;

public class Owm extends WeatherProvider {
    private OwmService mService;

    public Owm() {
        super("OWM", "https://openweathermap.org/",
                "http://api.openweathermap.org");
        mService = createService(OwmService.class);
    }

    @Override
    public List<WeatherData> getWeatherDataList(double latitude, double longitude) {
        List<WeatherData> weatherDataList = new ArrayList<>();

        try {
            OwmHourlyData owmHourlyData = mService.getLocationWeather(String.valueOf(latitude),
                    String.valueOf(longitude), "imperial", OwmAuth.getSecretKey())
                    .execute().body();
            if (owmHourlyData != null) {
                DateTime date = new DateTime();
                List<Hourly> forecastHourly = owmHourlyData.getHourly();
                // Iterate through all hourly list ~ 40
                int i = 0, count = 0;
                float low = Float.MAX_VALUE, high = Float.MIN_VALUE;
                Map<String, Integer> textMap = new HashMap<>();
                Map<String, Integer> srcMap = new HashMap<>();
                do {
                    Hourly forecast
                            = forecastHourly.get(i);
                    DateTime hourlyDateTime = new DateTime(forecast.getDt().longValue() * 1000);
                    if (date.withTimeAtStartOfDay()
                            .equals(hourlyDateTime.withTimeAtStartOfDay())) {
                        // Accumulate the whole hourly data
                        // Get min|max temp
                        Main main = forecast.getMain();
                        low = Math.min(low, main.getTempMin());
                        high = Math.max(high, main.getTempMax());
                        // Get text and src
                        Weather weather = forecast.getWeather().get(0);
                        String text = weather.getMain();
                        String src = weather.getIcon();
                        Integer textKey = textMap.get(text);
                        if (textKey != null)
                            textMap.put(text, textKey + 1);
                        else
                            textMap.put(text, 0);
                        Integer srcKey = srcMap.get(src);
                        if (srcKey != null)
                            srcMap.put(src, srcKey + 1);
                        else
                            srcMap.put(src, 0);
                        i++;
                    } else if(i == 0) {
                        // System clock is wrong
                        date = hourlyDateTime;
                    } else {
                        // Build WeatherData
                        WeatherData.Builder weatherDataBuilder = new WeatherData.Builder();
                        weatherDataBuilder
                                .withProviderId(getId())
                                .withDate((int) (date.getMillis() / 1000))
                                .withLow(low)
                                .withHigh(high)
                                .withText(getMostRepeatedString(textMap))
                                .withSrc(PATH + getMostRepeatedString(srcMap) + "@2x");
                        if (count == 0) {
                            OwmCurrentData owmCurrentData = mService
                                    .getCurrentWeather(String.valueOf(latitude),
                                            String.valueOf(longitude), "imperial",
                                            OwmAuth.getSecretKey()).execute().body();
                            if (owmCurrentData != null) {
                                weatherDataBuilder
                                        .isCurrent(true)
                                        .withCurrent(owmCurrentData.getMain().getTemp())
                                        .withTextExtra(owmCurrentData.getWeather()
                                                .get(0).getMain())
                                        .withSrcExtra(PATH + owmCurrentData.getWeather()
                                                .get(0).getIcon() + "@2x");
                            }
                        }
                        weatherDataList.add(weatherDataBuilder.build());
                        // Iterate next
                        low = Float.MAX_VALUE;
                        high = Float.MIN_VALUE;
                        textMap.clear();
                        srcMap.clear();
                        date = date.plusDays(1);
                        count++;
                    }
                } while (i < forecastHourly.size() && count < 5);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherDataList;
    }

    private String getMostRepeatedString(Map<String, Integer> srcMap) {
        Map.Entry<String, Integer> mostRepeated = null;

        for (Map.Entry<String, Integer> e : srcMap.entrySet()) {
            if (mostRepeated == null || mostRepeated.getValue() < e.getValue())
                mostRepeated = e;
        }

        return mostRepeated.getKey();
    }

    @Override
    public Observable<WeatherData> observeWeatherData(double latitude, double longitude) {
        return null;
    }
}
