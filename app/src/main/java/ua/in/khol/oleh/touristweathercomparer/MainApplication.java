package ua.in.khol.oleh.touristweathercomparer;

import android.app.Application;

import ua.in.khol.oleh.touristweathercomparer.di.ApplicationComponent;
import ua.in.khol.oleh.touristweathercomparer.di.ApplicationModule;
import ua.in.khol.oleh.touristweathercomparer.di.DaggerApplicationComponent;

public class MainApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Instantiating the components of dagger
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}