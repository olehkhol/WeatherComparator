package ua.in.khol.oleh.touristweathercomparer.ui.search;

import static ua.in.khol.oleh.touristweathercomparer.Globals.REQUEST_TIMEOUT;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableMaybeObserver;
import ua.in.khol.oleh.touristweathercomparer.data.database.DatabaseRepository;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;
import ua.in.khol.oleh.touristweathercomparer.data.places.PlacesRepository;
import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsRepository;
import ua.in.khol.oleh.touristweathercomparer.ui.DialogFragmentViewModel;
import ua.in.khol.oleh.touristweathercomparer.ui.ProgressEvent;

public class SearchViewModel extends DialogFragmentViewModel {

    // Repositories
    private final PlacesRepository placesRepository;
    private final DatabaseRepository databaseRepository;
    // Events
    private final MutableLiveData<List<String>> namesEvent = new MutableLiveData<>();
    // Vars
    private final String language;
    private final List<String> ids = new ArrayList<>();

    public SearchViewModel(
            SettingsRepository settingsRepository,
            PlacesRepository placesRepository,
            DatabaseRepository databaseRepository
    ) {
        super(settingsRepository);
        this.placesRepository = placesRepository;
        this.databaseRepository = databaseRepository;

        language = settingsRepository.retrieveLanguage();
    }

    public void predictPlaceNames(Observable<CharSequence> textChangesObservable) {
        execute(textChangesObservable
                        .debounce(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                        .switchMapMaybe(charSequence ->
                                placesRepository.predictPlaceNames(charSequence.toString())),
                pairs -> {
                    ids.clear();
                    List<String> names = new ArrayList<>();
                    for (Pair<String, String> pair : pairs) {
                        ids.add(pair.first);
                        names.add(pair.second);
                    }
                    namesEvent.setValue(names);
                });
    }

    public void processPosition(final int position) {
        List<String> names = namesEvent.getValue();
        if (names == null || names.size() <= position)
            return;

        final String name = namesEvent.getValue().get(position);

        progressEvent.postValue(ProgressEvent.PROGRESS);
        execute(placesRepository.seeLatLng(ids.get(position))
                .delay(REQUEST_TIMEOUT, TimeUnit.SECONDS), new DisposableMaybeObserver<>() {

            @Override
            public void onSuccess(Pair<Double, Double> pair) {
                insertPlaceIntoDB(new Place(name, pair.first, pair.second, language));
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

    public LiveData<List<String>> getNamesEvent() {
        return namesEvent;
    }
}