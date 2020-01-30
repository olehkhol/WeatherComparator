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
        int count = 0;

        try {
            // Generate a first item from "Current weather data"
            OwmCurrentData current = mService.getCurrentWeather(String.valueOf(latitude),
                    String.valueOf(longitude), "imperial", OwmAuth.getApiKey())
                    .execute().body();
            DateTime date = new DateTime(current.getDt().longValue() * 1000)
                    .withTimeAtStartOfDay();
            WeatherData.Builder builder = new WeatherData.Builder();
            builder
                    .withProviderId(getId())
                    .withDate((int) (date.getMillis() / 1000))
                    .withLow(current.getMain().getTempMin())
                    .withHigh(current.getMain().getTempMax())
                    .withText(current.getWeather().get(0).getMain())
                    .withSrc(PATH + current.getWeather().get(0).getIcon() + "@2x");
            builder
                    .isCurrent(true)
                    .withCurrent(current.getMain().getTemp())
                    .withTextExtra(current.getWeather().get(0).getMain())
                    .withSrcExtra(PATH + current.getWeather().get(0).getIcon() + "@2x");
            weatherDataList.add(builder.build());

            // Generate a list from "5 day weather forecast"
            OwmHourlyData hourly = mService.getLocationWeather(String.valueOf(latitude),
                    String.valueOf(longitude), "imperial", OwmAuth.getApiKey())
                    .execute().body();
            if (hourly != null) {
                List<Hourly> forecastHourly = hourly.getHourly();
                // Iterate through list ~ 40
                int i = 0;
                float low = Float.MAX_VALUE, high = Float.MIN_VALUE;
                Map<String, Integer> textMap = new HashMap<>();
                Map<String, Integer> srcMap = new HashMap<>();
                // Move through all array
                do {
                    DateTime hourlyDate = new DateTime(forecastHourly.get(i).getDt().longValue() * 1000)
                            .withTimeAtStartOfDay();
                    if (i < forecastHourly.size() && date.equals(hourlyDate)) {
                        // Accumulate the whole hourly data
                        Hourly forecast = forecastHourly.get(i);
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
                    } else if (i == 0) {
                        date = hourlyDate;
                    } else {
                        // Build WeatherData
                        builder = new WeatherData.Builder();
                        builder
                                .withProviderId(getId())
                                .withDate((int) (date.withTimeAtStartOfDay().getMillis() / 1000))
                                .withLow(low)
                                .withHigh(high)
                                .withText(getMostRepeatedString(textMap))
                                .withSrc(PATH + getMostRepeatedString(srcMap) + "@2x");

                        weatherDataList.add(builder.build());
                        // Iterate next
                        low = Float.MAX_VALUE;
                        high = Float.MIN_VALUE;
                        textMap.clear();
                        srcMap.clear();
                        date = date.plusDays(1);
                        count++;
                    }
                } while (count < DAYS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove a duplicate item
        if (weatherDataList.get(0).getDate() == weatherDataList.get(1).getDate())
            weatherDataList.remove(1);

        return weatherDataList.subList(0, DAYS);
    }

    private String getMostRepeatedString(Map<String, Integer> srcMap) {
        Map.Entry<String, Integer> mostRepeated = null;

        for (Map.Entry<String, Integer> e : srcMap.entrySet()) {
            if (mostRepeated == null || mostRepeated.getValue() < e.getValue())
                mostRepeated = e;
        }

        return mostRepeated.getKey();
    }

    // Stub
    @Override
    public Observable<WeatherData> observeWeatherData(double latitude, double longitude) {
        return null;
    }
}
