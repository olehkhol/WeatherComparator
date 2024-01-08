package ua.in.khol.oleh.touristweathercomparer.data.weather;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static ua.in.khol.oleh.touristweathercomparer.Globals.PATH_SEPARATOR;

public abstract class WeatherProvider implements WeatherRepository {

    private final String name;
    private final String site;
    private final String api;

    protected WeatherProvider(String name, String site, String api) {
        this.name = name;
        this.site = site;
        this.api = api;
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build();
    }

    protected <T> T createService(final Class<T> service) {
        return buildRetrofit()
                .create(service);
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .cache(null);

        return client.build();
    }

    protected String getPath() {
        return name.toLowerCase() + PATH_SEPARATOR;
    }
}