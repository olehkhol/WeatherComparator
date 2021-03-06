package ua.in.khol.oleh.touristweathercomparer.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.khol.oleh.touristweathercomparer.bus.RxBus;
import ua.in.khol.oleh.touristweathercomparer.model.GodRepository;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.cache.CacheHelper;
import ua.in.khol.oleh.touristweathercomparer.model.cache.RxCacheHelper;
import ua.in.khol.oleh.touristweathercomparer.model.db.AppDatabase;
import ua.in.khol.oleh.touristweathercomparer.model.db.DatabaseHelper;
import ua.in.khol.oleh.touristweathercomparer.model.db.RxDatabaseHelper;
import ua.in.khol.oleh.touristweathercomparer.model.location.RxLocationHelper;
import ua.in.khol.oleh.touristweathercomparer.model.maps.MapsHelper;
import ua.in.khol.oleh.touristweathercomparer.model.maps.RxMapsHelper;
import ua.in.khol.oleh.touristweathercomparer.model.net.RxNetwork;
import ua.in.khol.oleh.touristweathercomparer.model.net.RxNetworkHelper;
import ua.in.khol.oleh.touristweathercomparer.model.places.PlacesHelper;
import ua.in.khol.oleh.touristweathercomparer.model.places.RxPlacesHelper;
import ua.in.khol.oleh.touristweathercomparer.model.settings.RxPreferencesHelper;
import ua.in.khol.oleh.touristweathercomparer.model.weather.RxWeatherHelper;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherHelper;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ViewModelProviderFactory;

@Module
public class AppModule {
    private static final String DB_FILE_NAME = "wc.db";
    private static final String PREFERENCES_FILE_NAME = "wc.pref";

    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context provideApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    PlacesHelper providePlacesHelper(Context context) {
        return new RxPlacesHelper(context);
    }

    @Provides
    @Singleton
    @Named("db")
    String provideDatabaseName() {
        return DB_FILE_NAME;
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(Application application, @Named("db") String databaseName) {
        return Room.databaseBuilder(application, AppDatabase.class, databaseName)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    DatabaseHelper provideAppDbHelper(AppDatabase appDatabase) {
        return new RxDatabaseHelper(appDatabase);
    }

    @Provides
    @Singleton
    @Named("pref")
    String providePreferencesFileName() {
        return PREFERENCES_FILE_NAME;
    }

    @Provides
    @Singleton
    Boolean provideGoogleAvailability(Context context) {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
                == ConnectionResult.SUCCESS;
    }

    @Provides
    @Singleton
    RxLocationHelper provideLocationHelper(Context context, Boolean googleAvailability) {
        RxLocationHelper rxLocationHelper = new RxLocationHelper(context);
        int source = googleAvailability
                ? RxLocationHelper.GOOGLE_LOCATION : RxLocationHelper.ANDROID_LOCATION;
        rxLocationHelper.setup(source);

        return rxLocationHelper;
    }

    @Provides
    @Singleton
    RxNetwork provideNetwork(Context context) {
        return new RxNetworkHelper(context);
    }

    @Provides
    @Singleton
    MapsHelper provideMapsHelper() {
        return new RxMapsHelper();
    }

    @Provides
    @Singleton
    WeatherHelper provideWeatherHelper() {
        return new RxWeatherHelper();
    }

    @Provides
    @Singleton
    RxPreferencesHelper providePreferences(Context context) {
        return new RxPreferencesHelper(context);
    }

    @Provides
    @Singleton
    CacheHelper provideCacheHelper() {
        return new RxCacheHelper();
    }

    @Provides
    @Singleton
    RxBus provideRxBus() {
        return new RxBus();
    }

    @Provides
    @Singleton
    Repository provideGodRepository(RxBus rxBus,
                                    CacheHelper cache,
                                    RxLocationHelper location,
                                    MapsHelper maps,
                                    WeatherHelper weather,
                                    RxPreferencesHelper preferences,
                                    DatabaseHelper database,
                                    PlacesHelper places) {
        return new GodRepository(rxBus, cache, location, maps,
                weather, preferences, database, places);
    }

    @Provides
    @Singleton
    ViewModelProviderFactory provideViewModelProviderFactory(Repository repository) {
        return new ViewModelProviderFactory(repository);
    }
}
