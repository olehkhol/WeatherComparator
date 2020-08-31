package ua.in.khol.oleh.touristweathercomparer.model.weather;

import org.joda.time.DateTime;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class WeatherProvider {

    protected final String PATH;
    private final String NAME;
    private final String SITE;
    private final String API;
    private final String BANNER;
    private int mId;

    protected WeatherProvider(int id, String name, String site, String api) {
        mId = id;
        NAME = name;
        SITE = site;
        API = api;
        PATH = name.toLowerCase() + "/";
        BANNER = PATH + "banner";
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build();
    }

    protected <T> T createService(final Class<T> service) {
        return buildRetrofit()
                .create(service);
    }

    public abstract Forecast getCurrent(double latitude, double longitude, String lang);

    public abstract List<Forecast> getDaily(double latitude, double longitude, String lang);

    public String getName() {
        return NAME;
    }

    public String getSite() {
        return SITE;
    }

    public String getBanner() {
        return BANNER;
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .cache(null)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        return client.build();
    }

    protected String normalizeText(String text) {
        if (text != null && text.length() > 0)
            return text.substring(0, 1).toUpperCase() +
                    text.substring(1).trim().replaceAll("\\.+$", "").toLowerCase();
        else
            return text;
    }

    protected int getTime() {
        return (int) (new DateTime().getMillis() / 1000);
    }
}
