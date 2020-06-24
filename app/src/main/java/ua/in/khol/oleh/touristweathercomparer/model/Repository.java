package ua.in.khol.oleh.touristweathercomparer.model;

import android.util.Pair;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;

public interface Repository {

    enum Status {
        OFFLINE, CRITICAL_OFFLINE, ONLINE, REFRESHING, REFRESHED,
        NEED_RECREATE, ERROR, LOCATION_UNAVAILABLE, CLEAR
    }

    Observable<City> observeCity();

    Observable<Average> observeCurrent();

    Observable<List<Average>> observeAverages();

    Observable<Settings> observeSettings();

    Observable<Status> observeStatus();

    Observable<Place> observePlace();

    Observable<List<Forecast>> observeCurrents();

    Observable<List<List<Forecast>>> observeDailies();

    void setSettings(Settings settings);

    void processRefresh(boolean type);

    Settings getSettings();

    Single<List<Pair<String, String>>> predictPlacesList(String query);

    void processPlaceById(String placeId);

    void setLocation(LatLon latLon);

    boolean getPermissions();

    void setPermissions(boolean allowed);

}
