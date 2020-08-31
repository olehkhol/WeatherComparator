package ua.in.khol.oleh.touristweathercomparer.model.db;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Current;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Daily;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

public class RxDatabaseHelper implements DatabaseHelper {

    private final AppDatabase mAppDatabase;

    public RxDatabaseHelper(AppDatabase appDatabase) {
        mAppDatabase = appDatabase;
    }

    @Override
    public long putPlace(Place place) {
        return mAppDatabase.getPlaceDao().insertPlace(place);
    }

    @Override
    public void putDailies(List<Daily> dailies) {
        mAppDatabase.getDailyDao().insertDailies(dailies);
    }

    @Override
    public void putCurrents(List<Current> currents) {
        mAppDatabase.getCurrentDao().insertCurrents(currents);
    }

    @Override
    public Observable<List<Place>> observeLatestPlaces(int count) {
        return mAppDatabase.getPlaceDao().observeLatestPlaces(count);
    }

    @Override
    public Observable<Place> observeLatestPlace() {
        return mAppDatabase.getPlaceDao().observeLatestPlace();
    }

    @Override
    public Maybe<List<Current>> tryCurrents(long placeId, int date) {
        return mAppDatabase.getCurrentDao().tryCurrents(placeId, date)
                .filter(currents -> currents.size() > 0);
    }

    @Override
    public Single<Place> seePlace(long placeId) {
        return mAppDatabase.getPlaceDao().seePlace(placeId)
                .doOnError(Throwable::printStackTrace);
    }

    @Override
    public Maybe<List<Daily>> tryDailies(long placeId, int date) {
        return mAppDatabase.getDailyDao().tryDailies(placeId, date)
                .filter(dailies -> dailies.size() > 0);
    }

    @Override
    public Maybe<Place> tryPlace(double latitude, double longitude, String language) {
        return mAppDatabase.getPlaceDao().tryPlace(latitude, longitude, language)
                .doOnError(Throwable::printStackTrace);
    }
}
