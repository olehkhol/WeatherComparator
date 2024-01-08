package ua.in.khol.oleh.touristweathercomparer.data.database;

import androidx.paging.DataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.data.database.dao.CurrentDao;
import ua.in.khol.oleh.touristweathercomparer.data.database.dao.HourlyDao;
import ua.in.khol.oleh.touristweathercomparer.data.database.dao.PlaceDao;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;

public class DatabaseDataRepository implements DatabaseRepository {

    private final PlaceDao placeDao;
    private final CurrentDao currentDao;
    private final HourlyDao hourlyDao;

    public DatabaseDataRepository(ApplicationDatabase database) {
        placeDao = database.placeDao();
        currentDao = database.currentDao();
        hourlyDao = database.hourlyDao();
    }

    @Override
    public Completable insertPlace(Place place) {
        return placeDao.insertPlace(place);
    }

    @Override
    public Observable<Place> observePlace() {
        return placeDao.observePlace();
    }

    @Override
    public Observable<List<Place>> observePlaces(int limit) {
        return placeDao.observePlaces(limit)
                .filter(places -> places.size() > 0);
    }

    @Override
    public Completable updatePlaces(List<Place> places) {
        return placeDao.updatePlaces(places);
    }

    @Override
    public Completable deletePlace(Place place) {
        return placeDao.deletePlace(place);
    }

    @Override
    public DataSource.Factory<Integer, Place> placesById() {
        return placeDao.placesById();
    }

    @Override
    public Completable insertCurrent(Current current) {
        return currentDao.insertCurrent(current);
    }

    @Override
    public Maybe<Current> tryCurrent(Place place, String language, String units) {
        return currentDao.tryCurrent(place.latitude, place.longitude, language, units);
    }

    @Override
    public Completable insertHourlies(List<Hourly> hourlies) {
        return hourlyDao.insertHourlies(hourlies);
    }

    @Override
    public Maybe<List<Hourly>> tryHourlies(Place place, String language, String units) {
        return hourlyDao.tryHourlies(place.latitude, place.longitude, language, units);
    }
}