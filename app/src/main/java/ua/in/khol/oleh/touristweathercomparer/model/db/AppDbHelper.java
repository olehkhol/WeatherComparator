package ua.in.khol.oleh.touristweathercomparer.model.db;

import java.util.List;

import io.reactivex.Observable;

public class AppDbHelper implements DbHelper {

    private final AppDatabase mAppDatabase;

    public AppDbHelper(AppDatabase appDatabase) {
        mAppDatabase = appDatabase;
    }

    @Override
    public Observable<Long> putPlace(Place place) {
        return Observable.fromCallable(() -> mAppDatabase.getPlaceDao().insertPlace(place));
    }

    @Override
    public Observable<List<Place>> getAllPlaces() {
        return Observable.fromCallable(() -> mAppDatabase.getPlaceDao().loadPlaces());
    }

    @Override
    public Observable<Boolean> removePlace(int id) {
        return Observable.fromCallable(() -> {
            mAppDatabase.getPlaceDao().deletePlace(id);
            return true;
        });
    }

    @Override
    public Observable<Integer> removePlace(Place place) {
        return Observable.fromCallable(() -> mAppDatabase.getPlaceDao().deletePlace(place));
    }

}
