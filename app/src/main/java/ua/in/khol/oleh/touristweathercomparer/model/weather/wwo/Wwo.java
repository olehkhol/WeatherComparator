package ua.in.khol.oleh.touristweathercomparer.model.weather.wwo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import ua.in.khol.oleh.touristweathercomparer.model.weather.AbstractProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo.CurrentConditionItem;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo.WWOData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.pojo.WeatherItem;

public class Wwo extends AbstractProvider {
    private WwoService mService;

    public Wwo() {
        super("WWO", "https://www.worldweatheronline.com/",
                "https://api.worldweatheronline.com");
        mService = createService(WwoService.class);
    }

    @Override
    public Observable<ProviderData> getWeatherObservable(double latitude,
                                                         double longitude) {
        String language = getLanguage();

        Observable<WWOData> observable = mService
                .getLocationWeather(latitude + "," + longitude,
                        WwoAuth.getPremiumKey(),
                        language,
                        "7",
                        "json",
                        "12");


        return observable.map(WWOData -> {
            List<WeatherData> weatherDataList = new ArrayList<>();
            List<WeatherItem> forecastList = WWOData.getData().getWeather();
            CurrentConditionItem condition
                    = WWOData.getData().getCurrentCondition().get(0);

            int count = 0;
            for (WeatherItem item : forecastList) {
                if (count < DAYS) {
                    WeatherData.Builder builder = new WeatherData.Builder();
                    builder
                            .withDate(Wwo.this.getDate(item))
                            .withLow(Float.parseFloat(Wwo.this.getLow(item)))
                            .withHigh(Float.parseFloat(Wwo.this.getHigh(item)))
                            .withText(Wwo.this.getText(item))
                            .withSrc(Wwo.this.getSrc(item));

                    if (count == 0) {
                        builder
                                .withCurrent(Float.parseFloat(Wwo.this.getCurrent(condition)))
                                .withWind(Wwo.this.getWindSpeed(condition))
                                .withHumidity(condition.getHumidity())
                                .withTextExtra(Wwo.this.getTextExtra(condition))
                                .withSrcExtra(Wwo.this.getSrcExtra(condition));
                    }

                    weatherDataList.add(builder.build());
                    count++;
                }
            }

            return Wwo.this.compositeProviderData(weatherDataList);
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
