package ua.in.khol.oleh.touristweathercomparer.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.khol.oleh.touristweathercomparer.model.GodRepository;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.db.AppDatabase;
import ua.in.khol.oleh.touristweathercomparer.model.db.AppDbHelper;
import ua.in.khol.oleh.touristweathercomparer.model.db.DbHelper;
import ua.in.khol.oleh.touristweathercomparer.model.location.AppLocationHelper;
import ua.in.khol.oleh.touristweathercomparer.model.location.LocationHelper;
import ua.in.khol.oleh.touristweathercomparer.model.preferences.AppPreferencesHelper;
import ua.in.khol.oleh.touristweathercomparer.model.preferences.PreferencesHelper;
import ua.in.khol.oleh.touristweathercomparer.utils.Constants;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ViewModelProviderFactory;

@Module
public class AppModule {
    private Application mApplication;

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
    Context provideBaseContext(Application application) {
        return application.getBaseContext();
    }

    @Provides
    @Singleton
    @Named("db")
    String provideDatabaseName() {
        return Constants.DB_FILE_NAME;
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
    DbHelper provideAppDbHelper(AppDatabase appDatabase) {
        return new AppDbHelper(appDatabase);
    }

    @Provides
    @Singleton
    @Named("pref")
    String providePreferencesFileName() {
        return Constants.PREFERENCES_FILE_NAME;
    }


    @Provides
    @Singleton
    LocationHelper provideLocationHelper(Context context) {
        return new AppLocationHelper(context);
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(Context context,
                                               @Named("pref") String preferencesFileName) {
        return new AppPreferencesHelper(context, preferencesFileName);
    }

    @Provides
    @Singleton
    Repository provideGodRepository(Application application,
                                    LocationHelper locationHelper,
                                    PreferencesHelper preferencesHelper) {
        return new GodRepository(application, locationHelper, preferencesHelper);
    }

    @Provides
    @Singleton
    ViewModelProviderFactory provideViewModelProviderFactory(Repository repository) {
        return new ViewModelProviderFactory(repository);
    }

}
