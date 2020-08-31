package ua.in.khol.oleh.touristweathercomparer.model.db;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Current;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Daily;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

public interface DatabaseHelper {

    long putPlace(Place place);

    void putCurrents(List<Current> currents);

    void putDailies(List<Daily> dailies);

    Observable<List<Place>> observeLatestPlaces(int count);

    Observable<Place> observeLatestPlace();

    Maybe<List<Current>> tryCurrents(long placeId, int time);

    Single<Place> seePlace(long placeId);

    Maybe<List<Daily>> tryDailies(long placeId, int date);

    Maybe<Place> tryPlace(double latitude, double longitude, String language);

}
