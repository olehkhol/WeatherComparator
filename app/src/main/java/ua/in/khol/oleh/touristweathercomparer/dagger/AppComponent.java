package ua.in.khol.oleh.touristweathercomparer.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.khol.oleh.touristweathercomparer.views.MainView;
import ua.in.khol.oleh.touristweathercomparer.views.SettingsView;

@Component(modules = {AppModule.class, RepositoryModule.class})
@Singleton
public interface AppComponent {
    void inject(MainView mainActivity);

    void inject(SettingsView settingsView);
}
