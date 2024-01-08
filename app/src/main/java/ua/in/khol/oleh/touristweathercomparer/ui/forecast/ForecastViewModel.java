package ua.in.khol.oleh.touristweathercomparer.ui.forecast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.functions.Consumer;
import ua.in.khol.oleh.touristweathercomparer.data.database.DatabaseRepository;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;
import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsRepository;
import ua.in.khol.oleh.touristweathercomparer.ui.FragmentViewModel;

public class ForecastViewModel extends FragmentViewModel {

    private static final int AVERAGES_LIMIT = 3;

    private final DatabaseRepository databaseRepository;
    private final MutableLiveData<List<Place>> placesEvent = new MutableLiveData<>();

    public ForecastViewModel(
            SettingsRepository settingsRepository,
            DatabaseRepository databaseRepository
    ) {
        super(settingsRepository);
        this.databaseRepository = databaseRepository;

        observePlaces();
    }

    private void observePlaces() {
        execute(
                databaseRepository.observePlaces(AVERAGES_LIMIT),
                placesEvent::setValue
        );
    }

    public LiveData<List<Place>> getPlacesEvent() {
        return placesEvent;
    }
}