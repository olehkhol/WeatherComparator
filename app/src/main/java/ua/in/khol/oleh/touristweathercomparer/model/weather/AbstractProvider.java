package ua.in.khol.oleh.touristweathercomparer.model.weather;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class AbstractProvider {

    private String NAME;
    private String SITE;
    protected String API;
    protected String PATH;
    protected final static int DAYS = 7;

    public AbstractProvider(String name, String site, String api) {
        NAME = name;
        SITE = site;
        API = api;
        PATH = name.toLowerCase() + "/";
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


    public abstract Observable<ProviderData> getWeatherObservable(double latitude,
                                                                  double longitude);

    protected String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    protected ProviderData compositeProviderData(List<WeatherData> weatherDataList) {
        return new ProviderData(NAME,
                SITE,
                PATH + "banner",
                weatherDataList);
    }

    public String getName() {
        return NAME;
    }

    protected OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60 / 2, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(null)
                .build();
    }
}
