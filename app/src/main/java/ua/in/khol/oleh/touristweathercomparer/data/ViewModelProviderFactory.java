package ua.in.khol.oleh.touristweathercomparer.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ua.in.khol.oleh.touristweathercomparer.data.database.DatabaseRepository;
import ua.in.khol.oleh.touristweathercomparer.data.geocoding.GeocodingRepository;
import ua.in.khol.oleh.touristweathercomparer.data.places.PlacesRepository;
import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsRepository;
import ua.in.khol.oleh.touristweathercomparer.data.weather.WeatherRepository;
import ua.in.khol.oleh.touristweathercomparer.ui.average.AverageViewModel;
import ua.in.khol.oleh.touristweathercomparer.ui.forecast.ForecastViewModel;
import ua.in.khol.oleh.touristweathercomparer.ui.history.HistoryViewModel;
import ua.in.khol.oleh.touristweathercomparer.ui.main.MainViewModel;
import ua.in.khol.oleh.touristweathercomparer.ui.map.MapViewModel;
import ua.in.khol.oleh.touristweathercomparer.ui.search.SearchViewModel;
import ua.in.khol.oleh.touristweathercomparer.ui.settings.PreferenceViewModel;
import ua.in.khol.oleh.touristweathercomparer.ui.settings.SettingsViewModel;

public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {

    private final SettingsRepository settingsRepository;
    private final DatabaseRepository databaseRepository;
    private final GeocodingRepository geocodingRepository;
    private final PlacesRepository placesRepository;
    private final WeatherRepository weatherRepository;

    public ViewModelProviderFactory(SettingsRepository settingsRepository,
                                    DatabaseRepository databaseRepository,
                                    GeocodingRepository geocodingRepository,
                                    PlacesRepository placesRepository,
                                    WeatherRepository weatherRepository) {
        this.settingsRepository = settingsRepository;
        this.databaseRepository = databaseRepository;
        this.geocodingRepository = geocodingRepository;
        this.placesRepository = placesRepository;
        this.weatherRepository = weatherRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(settingsRepository);
        } else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(settingsRepository);
        } else if (modelClass.isAssignableFrom(PreferenceViewModel.class)) {
            return (T) new PreferenceViewModel();
        } else if (modelClass.isAssignableFrom(ForecastViewModel.class)) {
            return (T) new ForecastViewModel(settingsRepository, databaseRepository);
        } else if (modelClass.isAssignableFrom(AverageViewModel.class)) {
            return (T) new AverageViewModel(
                    settingsRepository,
                    databaseRepository,
                    weatherRepository
            );
        } else if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(
                    settingsRepository,
                    databaseRepository,
                    weatherRepository,
                    geocodingRepository
            );
        } else if (modelClass.isAssignableFrom(HistoryViewModel.class)) {
            return (T) new HistoryViewModel(settingsRepository, databaseRepository);
        } else if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(
                    settingsRepository,
                    placesRepository,
                    databaseRepository
            );
        }

        throw new RuntimeException(modelClass.getName());
    }
}