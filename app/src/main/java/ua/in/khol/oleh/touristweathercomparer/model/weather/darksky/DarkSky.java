package ua.in.khol.oleh.touristweathercomparer.model.weather.darksky;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.model.weather.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.currently.Currently;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.currently.DarkSkyCurrently;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.daily.DarkSkyDaily;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.pojo.daily.Datum;

public class DarkSky extends WeatherProvider {
    private final DarkSkyService mService;

    public DarkSky(int id) {
        super(id, "DarkSky", "https://darksky.net/poweredby/",
                "https://api.darksky.net");
        mService = createService(DarkSkyService.class);
    }

    @Override
    public Forecast getCurrent(double latitude, double longitude, String lang) {
        try {
            DarkSkyCurrently darkSkyCurrently = mService.getCurrently(DarkSkyAuth.getSecretKey(),
                    String.valueOf(latitude), String.valueOf(longitude), lang, "us")
                    .execute().body();
            if (darkSkyCurrently != null) {

                Currently currently = darkSkyCurrently.getCurrently();
                Forecast.Builder builder = new Forecast.Builder();
                builder
                        .withDate(getTime())
                        .withLow(currently.getTemperature())
                        .withHigh(currently.getTemperature())
                        .withText(normalizeText(currently.getSummary()))
                        .withSrc(PATH + currently.getIcon())
                        .withPressure(currently.getPressure())
                        .withSpeed(currently.getWindSpeed())
                        .withDegree(currently.getWindBearing())
                        .withHumidity((int) (currently.getHumidity() * 100))
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

        try {
            DarkSkyDaily darkSkyData = mService.getDaily(DarkSkyAuth.getSecretKey(),
                    String.valueOf(latitude), String.valueOf(longitude),
                    lang, "us")
                    .execute().body();

            if (darkSkyData != null) {
                List<Datum> datumList = darkSkyData.getDaily().getData();
                for (Datum datum : datumList) {
                    long time = (long) datum.getTime() * 1000;
                    DateTime dt = new DateTime(time).withTimeAtStartOfDay();
                    Forecast.Builder builder = new Forecast.Builder();
                    builder
                            .withDate((int) (dt.getMillis() / 1000))
                            .withLow(datum.getTemperatureMin())
                            .withHigh(datum.getTemperatureMax())
                            .withText(normalizeText(datum.getSummary()))
                            .withSrc(PATH + datum.getIcon())
                            .withPressure(datum.getPressure())
                            .withSpeed(datum.getWindSpeed())
                            .withDegree(datum.getWindBearing())
                            .withHumidity((int) (datum.getHumidity() * 100))
                            .withCurrent(false);
                    forecastList.add(builder.build());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return forecastList;

    }
}
