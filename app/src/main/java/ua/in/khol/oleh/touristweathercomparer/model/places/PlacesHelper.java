package ua.in.khol.oleh.touristweathercomparer.model.places;

import android.util.Pair;

import java.util.List;

import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;

public interface PlacesHelper {
    Single<List<Pair<String, String>>> seePlacesList(String query);

    Single<LatLon> seeLatlon(String placeId);
}
