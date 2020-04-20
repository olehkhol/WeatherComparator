package ua.in.khol.oleh.touristweathercomparer.di;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.di.modules.AlertViewModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.ForecastViewModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.InfoViewModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.MainViewModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.MapaViewModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.SettingsViewModule;

@Singleton
@Component(modules = {AppModule.class,
        MainViewModule.class,
        SettingsViewModule.class,
        ForecastViewModule.class,
        MapaViewModule.class,
        InfoViewModule.class,
        AlertViewModule.class})

public interface AppComponent {
    void inject(MainApplication mainApplication);
}
