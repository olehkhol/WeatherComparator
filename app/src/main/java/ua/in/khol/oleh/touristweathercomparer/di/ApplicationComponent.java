package ua.in.khol.oleh.touristweathercomparer.di;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.ui.average.AverageView;
import ua.in.khol.oleh.touristweathercomparer.ui.forecast.ForecastView;
import ua.in.khol.oleh.touristweathercomparer.ui.history.HistoryView;
import ua.in.khol.oleh.touristweathercomparer.ui.main.MainView;
import ua.in.khol.oleh.touristweathercomparer.ui.map.MapView;
import ua.in.khol.oleh.touristweathercomparer.ui.search.SearchView;
import ua.in.khol.oleh.touristweathercomparer.ui.settings.PreferenceView;
import ua.in.khol.oleh.touristweathercomparer.ui.settings.SettingsView;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(MainApplication mainApplication);

    void inject(MainView mainView);

    void inject(SettingsView settingsView);

    void inject(SearchView searchView);

    void inject(PreferenceView preferenceView);

    void inject(ForecastView forecastView);

    void inject(AverageView averageView);

    void inject(MapView mapView);

    void inject(HistoryView historyView);
}