package ua.in.khol.oleh.touristweathercomparer.data.database;

import androidx.paging.DataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;

public interface DatabaseRepository {

    Completable insertPlace(Place place);

    Observable<Place> observePlace();

    Observable<List<Place>> observePlaces(int limit);

    Completable updatePlaces(List<Place> places);

    Completable deletePlace(Place place);

    DataSource.Factory<Integer, Place> placesById();

    Completable insertCurrent(Current current);

    Maybe<Current> tryCurrent(Place place, String language, String units);

    Completable insertHourlies(List<Hourly> hourlies);

    Maybe<List<Hourly>> tryHourlies(Place place, String language, String units);
}