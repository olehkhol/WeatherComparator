package ua.in.khol.oleh.touristweathercomparer.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.observers.DisposableMaybeObserver;
import ua.in.khol.oleh.touristweathercomparer.data.database.DatabaseRepository;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;
import ua.in.khol.oleh.touristweathercomparer.data.geocoding.GeocodingRepository;
import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsRepository;
import ua.in.khol.oleh.touristweathercomparer.data.weather.WeatherRepository;
import ua.in.khol.oleh.touristweathercomparer.ui.FragmentViewModel;
import ua.in.khol.oleh.touristweathercomparer.ui.ProgressEvent;

public class MapViewModel extends FragmentViewModel {

    // Repositories
    private final DatabaseRepository databaseRepository;
    private final WeatherRepository weatherRepository;
    private final GeocodingRepository geocodingRepository;
    // Events
    private final MutableLiveData<Place> placeEvent = new MutableLiveData<>();
    private final MutableLiveData<Current> currentEvent = new MutableLiveData<>();
    // Vars
    private final String language;
    private final String units;

    public MapViewModel(SettingsRepository settingsRepository,
                        DatabaseRepository databaseRepository,
                        WeatherRepository weatherRepository,
                        GeocodingRepository geocodingRepository) {
        super(settingsRepository);
        this.databaseRepository = databaseRepository;
        this.weatherRepository = weatherRepository;
        this.geocodingRepository = geocodingRepository;

        language = settingsRepository.retrieveLanguage();
        units = settingsRepository.retrieveUnits();

        observePlace();
    }

    public void observePlace() {
        execute(
                databaseRepository.observePlace(),
                place -> {
                    placeEvent.setValue(place);
                    processPlace(place);
                }
        );
    }

    public void processPlace(final Place place) {
        predictCurrent(place);
    }

    private void predictCurrent(final Place place) {
        // Save `current` to database
        execute(weatherRepository.tryCurrent(place, language, units)
                .doOnSuccess(this::insertCurrentIntoDB)
                .onErrorResumeNext(throwable -> {
                    // Read `current` from database
                    return databaseRepository.tryCurrent(place, language, units);
                }), currentEvent::setValue);
    }

    public void predictPlace(final double latitude, final double longitude) {
        progressEvent.setValue(ProgressEvent.PROGRESS);
        execute(geocodingRepository.tryLocationName(latitude, longitude, language),
                new DisposableMaybeObserver<>() {

                    @Override
                    public void onSuccess(String name) {
                        insertPlaceIntoDB(new Place(name, latitude, longitude, language));
                        progressEvent.setValue(ProgressEvent.SUCCESS);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressEvent.setValue(ProgressEvent.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        progressEvent.setValue(ProgressEvent.IDLE);
                    }
                });
    }

    private void insertPlaceIntoDB(Place place) {
        execute(databaseRepository.insertPlace(place));
    }

    private void insertCurrentIntoDB(Current current) {
        execute(databaseRepository.insertCurrent(current));
    }

    public LiveData<Place> getPlaceEvent() {
        return placeEvent;
    }

    public LiveData<Current> getCurrentEvent() {
        return currentEvent;
    }
}