package ua.in.khol.oleh.touristweathercomparer.ui.average;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.data.database.DatabaseRepository;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;
import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsRepository;
import ua.in.khol.oleh.touristweathercomparer.data.weather.WeatherRepository;
import ua.in.khol.oleh.touristweathercomparer.ui.FragmentViewModel;

public class AverageViewModel extends FragmentViewModel {

    // Repositories
    private final DatabaseRepository databaseRepository;
    private final WeatherRepository weatherRepository;
    // Events
    private final MutableLiveData<Current> currentEvent = new MutableLiveData<>();
    private final MutableLiveData<List<Hourly>> hourliesEvent = new MutableLiveData<>();
    // Vars
    private final String language;
    private final String units;

    public AverageViewModel(SettingsRepository settingsRepository,
                            DatabaseRepository databaseRepository,
                            WeatherRepository weatherRepository) {
        super(settingsRepository);
        this.databaseRepository = databaseRepository;
        this.weatherRepository = weatherRepository;

        language = settingsRepository.retrieveLanguage();
        units = settingsRepository.retrieveUnits();
    }

    public void processPlace(final Place place) {
        predictCurrent(place);
        predictHourlies(place);
    }

    private void predictCurrent(final Place place) {
        // Save `current` to database
        execute(weatherRepository.tryCurrent(place, language, units)
                .doOnSuccess(this::insertCurrent)
                .onErrorResumeNext(throwable -> {
                    // Read `current` from database
                    return databaseRepository.tryCurrent(place, language, units);
                }), currentEvent::setValue);
    }

    private void predictHourlies(final Place place) {
        // Save `averages` to database
        execute(weatherRepository.tryHourlies(place, language, units)
                .doOnSuccess(this::insertHourlies)
                .onErrorResumeNext(throwable -> {
                    // Read `hourlies` from database
                    return databaseRepository.tryHourlies(place, language, units);
                }), hourliesEvent::setValue);
    }

    private void insertCurrent(Current current) {
        execute(databaseRepository.insertCurrent(current));
    }

    private void insertHourlies(List<Hourly> hourlies) {
        execute(databaseRepository.insertHourlies(hourlies));
    }

    public MutableLiveData<Current> getCurrentEvent() {
        return currentEvent;
    }

    public MutableLiveData<List<Hourly>> getHourliesEvent() {
        return hourliesEvent;
    }
}