package ua.in.khol.oleh.touristweathercomparer.model.weather.owm;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ua.in.khol.oleh.touristweathercomparer.model.weather.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.pojo.current.OwmCurrentData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.pojo.hourly.Hourly;
import ua.in.khol.oleh.touristweathercomparer.model.weather.owm.pojo.hourly.OwmHourlyData;

public class Owm extends WeatherProvider {
    private final OwmService mService;

    public Owm(int id) {
        super(id, "OWM", "https://openweathermap.org/",
                "http://api.openweathermap.org");
        mService = createService(OwmService.class);
    }

    @Override
    public Forecast getCurrent(double latitude, double longitude, String lang) {
        try {
            OwmCurrentData currentData = mService.getCurrentWeather(String.valueOf(latitude),
                    String.valueOf(longitude), lang, "imperial", OwmAuth.getApiKey())
                    .execute().body();
            if (currentData != null) {
                Forecast.Builder builder = new Forecast.Builder();
                builder
                        .withDate(getTime())
                        .withLow(currentData.getMain().getTemp())
                        .withHigh(currentData.getMain().getTemp())
                        .withText(normalizeText(currentData.getWeather().get(0).getDescription()))
                        .withSrc(PATH + currentData.getWeather().get(0).getIcon() + "@2x")
                        .withPressure(currentData.getMain().getPressure())
                        .withSpeed(currentData.getWind().getSpeed())
                        .withDegree(currentData.getWind().getDeg())
                        .withHumidity(currentData.getMain().getHumidity())
                        .withCurrent(true);

                return builder.build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Forecast> getDaily(double latitude, double longitude, String lang) {
        List<Forecast> forecastList = new ArrayList<>();
        Map<DateTime, List<Forecast>> map = new LinkedHashMap<>();

        try {
            // Get current
            OwmCurrentData currentData = mService.getCurrentWeather(String.valueOf(latitude),
                    String.valueOf(longitude), lang, "imperial", OwmAuth.getApiKey())
                    .execute().body();
            if (currentData != null) {
                long time = (long) currentData.getDt() * 1000;
                DateTime dt = new DateTime(time).withTimeAtStartOfDay();
                Forecast.Builder builder = new Forecast.Builder();
                builder
                        .withDate((int) (dt.getMillis() / 1000))
                        .withLow(currentData.getMain().getTempMin())
                        .withHigh(currentData.getMain().getTempMax())
                        .withText(normalizeText(currentData.getWeather().get(0).getDescription()))
                        .withSrc(PATH + currentData.getWeather().get(0).getIcon() + "@2x")
                        .withPressure(currentData.getMain().getPressure())
                        .withSpeed(currentData.getWind().getSpeed())
                        .withDegree(currentData.getWind().getDeg())
                        .withHumidity(currentData.getMain().getHumidity())
                        .withCurrent(false);
                addToMap(map, dt, builder.build());
            }

            // Generate list from "5 day weather forecast"
            OwmHourlyData hourlyData = mService.getLocationWeather(String.valueOf(latitude),
                    String.valueOf(longitude), lang, "imperial", OwmAuth.getApiKey())
                    .execute().body();
            if (hourlyData != null && hourlyData.getCnt() > 0) {
                for (Hourly hourly : hourlyData.getList()) {
                    long time = (long) hourly.getDt() * 1000;
                    DateTime dt = new DateTime(time).withTimeAtStartOfDay();
                    Forecast wd = new Forecast.Builder()
                            .withDate((int) (dt.getMillis() / 1000))
                            .withLow(hourly.getMain().getTempMin())
                            .withHigh(hourly.getMain().getTempMax())
                            .withText(normalizeText(hourly.getWeather().get(0).getDescription()))
                            .withSrc(PATH + hourly.getWeather().get(0).getIcon() + "@2x")
                            .withPressure(hourly.getMain().getPressure())
                            .withSpeed(hourly.getWind().getSpeed())
                            .withDegree(hourly.getWind().getDeg())
                            .withHumidity(hourly.getMain().getHumidity())
                            .withCurrent(false).build();
                    addToMap(map, dt, wd);
                }

                for (Map.Entry<DateTime, List<Forecast>> entry : map.entrySet())
                    forecastList.add(calculateAverage(entry));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove a duplicate item
        if (forecastList.size() > 0) {
            if (forecastList.get(0).getDate() == forecastList.get(1).getDate())
                forecastList.remove(1);
        }

        return forecastList;
    }

    private void addToMap(Map<DateTime, List<Forecast>> map, DateTime dt, Forecast wd) {
        List<Forecast> list = map.get(dt);
        if (list == null)
            list = new ArrayList<>();
        list.add(wd);
        map.put(dt, list);
    }

    private Forecast calculateAverage(Map.Entry<DateTime, List<Forecast>> entry) {
        int date = (int) (entry.getKey().getMillis() / 1000);
        List<Forecast> list = entry.getValue();
        int size = list.size();

        float[] lows = new float[size];
        float[] highs = new float[size];
        Map<String, Integer> textMap = new HashMap<>();
        Map<String, Integer> srcMap = new HashMap<>();
        float[] pressures = new float[size];
        float[] speeds = new float[size];
        int[] degrees = new int[size];
        int[] humidities = new int[size];

        for (int i = 0; i < size; i++) {
            Forecast wd = list.get(i);
            lows[i] = wd.getLow();
            highs[i] = wd.getHigh();

            String text = wd.getText();
            Integer textKey = textMap.get(text);
            if (textKey != null)
                textMap.put(text, textKey + 1);
            else
                textMap.put(text, 0);
            String src = wd.getSrc();
            Integer srcKey = srcMap.get(src);
            if (srcKey != null)
                srcMap.put(src, srcKey + 1);
            else
                srcMap.put(src, 0);

            pressures[i] = wd.getPressure();
            speeds[i] = wd.getSpeed();
            degrees[i] = wd.getDegree();
            humidities[i] = wd.getHumidity();
        }

        Arrays.sort(lows);
        Arrays.sort(highs);

        return new Forecast.Builder()
                .withDate(date)
                .withLow(lows[0])
                .withHigh(highs[size - 1])
                .withText(getMostRepeatedString(textMap))
                .withSrc(getMostRepeatedString(srcMap))
                .withPressure(averageFloat(pressures))
                .withSpeed(averageFloat(speeds))
                .withDegree(averageInt(degrees))
                .withHumidity(averageInt(humidities))
                .withCurrent(false)
                .build();
    }

    private int averageInt(int[] ints) {
        int length = ints.length;
        if (length == 0)
            throw new IllegalArgumentException("The length of an array is zero!");
        int result = 0;

        for (int i : ints) result += i;

        return result / length;
    }

    private float averageFloat(float[] floats) {
        int length = floats.length;
        if (length == 0)
            throw new IllegalArgumentException("The length of an array is zero!");
        float result = 0;

        for (float f : floats) result += f;

        return result / length;
    }

    private String getMostRepeatedString(Map<String, Integer> srcMap) {
        Map.Entry<String, Integer> mostRepeated = null;

        for (Map.Entry<String, Integer> e : srcMap.entrySet()) {
            if (mostRepeated == null || mostRepeated.getValue() < e.getValue())
                mostRepeated = e;
        }

        return mostRepeated.getKey();
    }
}
