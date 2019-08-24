package ua.in.khol.oleh.touristweathercomparer.model.db;

import java.util.List;

import io.reactivex.Observable;

public interface DbHelper {
    Observable<Long> putPlace(final Place place);

    Observable<List<Place>> getAllPlaces();

    Observable<Boolean> removePlace(final int id);

    Observable<Integer> removePlace(final Place place);
}
