package ua.in.khol.oleh.touristweathercomparer.dagger;

import android.app.Application;

import androidx.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;

@Module
public class RepositoryModule {

    @Provides
    @NonNull
    @Singleton
    Repository provideRepository(Application application) {
        return new Repository(application);
    }
}
