package ua.in.khol.oleh.touristweathercomparer.model;

import android.util.Pair;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;

public interface Repository {

    void coldRefresh();

    void hotRefresh();

    Observable<Object> observeBus();

    Observable<Place> observePlace();

    Observable<Place> observeLatestPlace();

    Maybe<LatLon> tryLatLon();

    Maybe<Place> tryPlace(LatLon latLon);

    Maybe<Average> tryCurrent(long placeId);

    Maybe<List<Average>> tryDailies(long placeId);

    Settings getSettings();

    void setSettings(Settings settings);

    void setLocation(LatLon latLon);

    void processPlaceById(String placeId);

    Single<List<Pair<String, String>>> predictPlaceNames(String query);

    Observable<List<Place>> observeLatestPlaces(int placesCount);
}
