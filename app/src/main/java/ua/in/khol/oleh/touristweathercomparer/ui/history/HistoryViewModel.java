package ua.in.khol.oleh.touristweathercomparer.ui.history;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.data.database.DatabaseRepository;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;
import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsRepository;
import ua.in.khol.oleh.touristweathercomparer.ui.FragmentViewModel;

public class HistoryViewModel extends FragmentViewModel {

    private static final int PAGE_SIZE = 10;

    private final DatabaseRepository databaseRepository;
    private final LiveData<PagedList<Place>> pagedPlacesEvent;

    public HistoryViewModel(SettingsRepository settingsRepository, DatabaseRepository databaseRepository) {
        super(settingsRepository);
        this.databaseRepository = databaseRepository;

        DataSource.Factory<Integer, Place> dataSourceFactory = databaseRepository.placesById();
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PAGE_SIZE)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setEnablePlaceholders(false)
                .build();
        pagedPlacesEvent = new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }

    public LiveData<PagedList<Place>> getPagedPlacesEvent() {
        return pagedPlacesEvent;
    }

    public void swap(List<Place> places) {
        for (int i = 0; i < places.size() - 1; i++)
            places.get(i).swap(places.get(i + 1));

        execute(databaseRepository.updatePlaces(places));
    }

    public void remove(Place place) {
        execute(databaseRepository.deletePlace(place));
    }
}