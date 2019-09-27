package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo.CurrentConditionItem;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo.WWOData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo.WeatherItem;

public class Wwo extends WeatherProvider {
    private WwoService mService;

    public Wwo() {
        super("WWO", "https://www.worldweatheronline.com/",
                "https://api.worldweatheronline.com");
        mService = createService(WwoService.class);
    }

    @Override
    public List<WeatherData> getWeatherDataList(double latitude, double longitude) {
        List<WeatherData> weatherDataList = new ArrayList<>();

        WWOData wwoData;
        try {
            wwoData = mService.getLocationWeather(latitude + "," + longitude,
                    WwoAuth.getPremiumKey(), "en", "7", "json", "12")
                    .execute().body();

            if (wwoData != null) {
                List<WeatherItem> forecastList = wwoData.getData().getWeather();
                CurrentConditionItem condition = wwoData.getData().getCurrentCondition().get(0);

                int count = 0;
                for (WeatherItem item : forecastList) {
                    if (count < DAYS) {
                        WeatherData.Builder builder = new WeatherData.Builder();
                        builder
                                .withProviderId(getId())
                                .withDate(getDate(item))
                                .withLow(Float.parseFloat(getLow(item)))
                                .withHigh(Float.parseFloat(getHigh(item)))
                                .withText(getText(item))
                                .withSrc(getSrc(item));

                        if (count == 0) {
                            builder
                                    .isCurrent(true)
                                    .withCurrent(Float.parseFloat(getCurrent(condition)))
                                    .withWind(getWindSpeed(condition))
                                    .withHumidity(condition.getHumidity())
                                    .withTextExtra(getTextExtra(condition))
                                    .withSrcExtra(getSrcExtra(condition));
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
    public Observable<WeatherData> observeWeatherData(double latitude,
                                                      double longitude) {
        Observable<WWOData> observable = mService.observeLocationWeather(latitude + "," + longitude,
                WwoAuth.getPremiumKey(), "en", "7", "json", "12");


        return observable.flatMap(WWOData -> {
            List<WeatherData> weatherDataList = new ArrayList<>();
            List<WeatherItem> forecastList = WWOData.getData().getWeather();
            CurrentConditionItem condition = WWOData.getData().getCurrentCondition().get(0);

            int count = 0;
            for (WeatherItem item : forecastList) {
                if (count < DAYS) {
                    WeatherData.Builder builder = new WeatherData.Builder();
                    builder
                            .withProviderId(getId())
                            .withDate(getDate(item))
                            .withLow(Float.parseFloat(getLow(item)))
                            .withHigh(Float.parseFloat(getHigh(item)))
                            .withText(getText(item))
                            .withSrc(getSrc(item));

                    if (count == 0) {
                        builder
                                .isCurrent(true)
                                .withCurrent(Float.parseFloat(getCurrent(condition)))
                                .withWind(getWindSpeed(condition))
                                .withHumidity(condition.getHumidity())
                                .withTextExtra(getTextExtra(condition))
                                .withSrcExtra(getSrcExtra(condition));
                    }

                    weatherDataList.add(builder.build());
                    count++;
                }
            }

            return Observable.fromIterable(weatherDataList);
        });
    }

    private int getDate(WeatherItem item) {
        String unformatted = item.getDate();

        int year = Integer.parseInt(unformatted.substring(0, 4));
        int month = Integer.parseInt(unformatted.substring(5, 7));
        int day = Integer.parseInt(unformatted.substring(8, 10));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);

        return (int) (cal.getTimeInMillis() / 1000L);
    }

    private String getCurrent(CurrentConditionItem condition) {
        return condition.getTempF();
    }

    private String getWindSpeed(CurrentConditionItem condition) {
        return condition.getWindspeedKmph();
        // condition.getWindspeedMiles()
    }

    private String getTextExtra(CurrentConditionItem condition) {
        String text = condition.getWeatherDesc().get(0).getValue();
        if (text.contains(","))
            return text.substring(0, text.indexOf(','));
        else
            return text;
    }

    private String getSrcExtra(CurrentConditionItem condition) {
        return PATH + condition.getWeatherCode();
    }

    private String getSrc(WeatherItem item) {
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int partOfDay = currentHour / 12;

        return PATH + item.getHourly().get(partOfDay).getWeatherCode();
    }

    private String getText(WeatherItem item) {
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int partOfDay = currentHour / 12;

        return item.getHourly().get(partOfDay).getWeatherDesc().get(0).getValue();
    }

    private String getLow(WeatherItem item) {
        return item.getMintempF();
    }

    private String getHigh(WeatherItem item) {
        return item.getMaxtempF();
    }

}
