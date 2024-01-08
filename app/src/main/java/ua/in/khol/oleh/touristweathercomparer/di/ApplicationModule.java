package ua.in.khol.oleh.touristweathercomparer.di;

import static ua.in.khol.oleh.touristweathercomparer.Globals.DB_FILE_NAME;
import static ua.in.khol.oleh.touristweathercomparer.Globals.MAPS_GOOGLEAPIS;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.room.Room;

import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.in.khol.oleh.touristweathercomparer.data.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.data.database.ApplicationDatabase;
import ua.in.khol.oleh.touristweathercomparer.data.database.DatabaseDataRepository;
import ua.in.khol.oleh.touristweathercomparer.data.database.DatabaseRepository;
import ua.in.khol.oleh.touristweathercomparer.data.geocoding.GeocodingDataRepository;
import ua.in.khol.oleh.touristweathercomparer.data.geocoding.GeocodingRepository;
import ua.in.khol.oleh.touristweathercomparer.data.places.PlacesDataRepository;
import ua.in.khol.oleh.touristweathercomparer.data.places.PlacesRepository;
import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsDataRepository;
import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsRepository;
import ua.in.khol.oleh.touristweathercomparer.data.weather.WeatherDataRepository;
import ua.in.khol.oleh.touristweathercomparer.data.weather.WeatherProvider;
import ua.in.khol.oleh.touristweathercomparer.data.weather.WeatherRepository;
import ua.in.khol.oleh.touristweathercomparer.data.weather.openweathermap.OpenWeatherProvider;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    SettingsRepository provideSettingsRepository(Context context) {
        return new SettingsDataRepository(context);
    }

    @Provides
    @Singleton
    ApplicationDatabase provideAppDatabase(Context context) {
        return Room
                .databaseBuilder(context, ApplicationDatabase.class, DB_FILE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    DatabaseRepository provideDatabaseRepository(ApplicationDatabase database) {
        return new DatabaseDataRepository(database);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(MAPS_GOOGLEAPIS)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();
    }

    @Provides
    @Singleton
    GeocodingRepository provideGeocodingRepository(Retrofit retrofit) {
        return new GeocodingDataRepository(retrofit);
    }

    @Provides
    @Singleton
    PlacesRepository providePlacesRepository(Context context) {
        return new PlacesDataRepository(context);
    }

    @Provides
    @Singleton
    WeatherProvider provideOpenWeatherProvider() {
        return new OpenWeatherProvider();
    }


    @Provides
    @Singleton
    WeatherRepository provideWeatherRepository(WeatherProvider weatherProvider) {
        return new WeatherDataRepository(weatherProvider);
    }

    @Provides
    @Singleton
    ViewModelProviderFactory provideViewModelProviderFactory(
            SettingsRepository settingsRepository,
            DatabaseRepository databaseRepository,
            GeocodingRepository geocodingRepository,
            PlacesRepository placesRepository,
            WeatherRepository weatherRepository
    ) {
        return new ViewModelProviderFactory(
                settingsRepository,
                databaseRepository,
                geocodingRepository,
                placesRepository,
                weatherRepository
        );
    }
}