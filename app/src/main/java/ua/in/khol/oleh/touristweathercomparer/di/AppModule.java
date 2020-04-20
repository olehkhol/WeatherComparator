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
import ua.in.khol.oleh.touristweathercomparer.model.settings.RxSettingsHelper;
import ua.in.khol.oleh.touristweathercomparer.model.weather.RxWeatherHelper;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherHelper;

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
    Integer provideGoogleAvailability(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int code = apiAvailability.isGooglePlayServicesAvailable(context);

        return code == ConnectionResult.SUCCESS
                ? RxLocationHelper.GOOGLE_LOCATION : RxLocationHelper.ANDROID_LOCATION;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context provideBaseContext(Application application) {
        return application.getBaseContext();
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
    RxLocationHelper provideLocationHelper(Context context, Integer googleAvailability) {
        RxLocationHelper rxLocationHelper = new RxLocationHelper(context);
        rxLocationHelper.setup(googleAvailability);

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
    RxSettingsHelper provideSettingsHelper(Context context) {
        return new RxSettingsHelper(context);
    }

    @Provides
    @Singleton
    CacheHelper provideCacheHelper() {
        return new RxCacheHelper();
    }

    @Provides
    @Singleton
    Repository provideGodRepository(CacheHelper cacheHelper,
                                    RxLocationHelper locationHelper,
                                    RxNetwork network,
                                    MapsHelper mapsHelper,
                                    WeatherHelper weatherHelper,
                                    RxSettingsHelper settingsHelper,
                                    DatabaseHelper databaseHelper) {
        return new GodRepository(cacheHelper, locationHelper, network, mapsHelper, weatherHelper,
                settingsHelper, databaseHelper);
    }

    @Provides
    @Singleton
    ViewModelProviderFactory provideViewModelProviderFactory(Repository repository) {
        return new ViewModelProviderFactory(repository);
    }
}
