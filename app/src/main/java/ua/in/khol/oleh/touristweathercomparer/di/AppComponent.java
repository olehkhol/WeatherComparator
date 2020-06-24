package ua.in.khol.oleh.touristweathercomparer.di;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.di.modules.AlertModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.ForecastModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.InfoModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.MainModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.MapaModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.SearchModule;
import ua.in.khol.oleh.touristweathercomparer.di.modules.SettingsModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,
        AppModule.class,

        AlertModule.class,
        ForecastModule.class,
        InfoModule.class,
        MainModule.class,
        MapaModule.class,
        SearchModule.class,
        SettingsModule.class})
public interface AppComponent {
    void inject(MainApplication mainApplication);
}
