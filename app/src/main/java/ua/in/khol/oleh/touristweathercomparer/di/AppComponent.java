package ua.in.khol.oleh.touristweathercomparer.di;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.khol.oleh.touristweathercomparer.MainApplication;

@Singleton
@Component(modules = {AppModule.class,
        MainViewModule.class,
        SettingsViewModule.class,
        HomeViewModule.class,
        AlertLocationViewModule.class})

public interface AppComponent {
    void inject(MainApplication mainApplication);
}
