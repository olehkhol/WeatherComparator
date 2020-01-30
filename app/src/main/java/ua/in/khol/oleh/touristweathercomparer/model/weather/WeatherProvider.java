package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class WeatherProvider {

    private String NAME;
    private String SITE;
    protected String API;
    protected String PATH;
    private String BANNER;
    protected final static int DAYS = 5;
    private static int sId = 0;
    private int mId;
    private boolean mCached;

    public WeatherProvider(String name, String site, String api) {
        mId = sId++;
        NAME = name;
        SITE = site;
        API = api;
        PATH = name.toLowerCase() + "/";
        BANNER = PATH + "banner";
    }

    public int getId() {
        return mId;
    }

    protected Retrofit buildRetrofit() {
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

    public abstract List<WeatherData> getWeatherDataList(double latitude, double longitude);

    public abstract Observable<WeatherData> observeWeatherData(double latitude, double longitude);

    public String getName() {
        return NAME;
    }

    public String getSite() {
        return SITE;
    }

    public String getBanner() {
        return BANNER;
    }

    protected OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60 / 2, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(null)
                .build();
    }

    public void setCached(boolean cached) {
        mCached = cached;
    }

    public boolean isCached() {
        return mCached;
    }
}
