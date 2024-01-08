package ua.in.khol.oleh.touristweathercomparer.data.places;

import androidx.core.util.Pair;

import java.util.List;

import io.reactivex.Maybe;

public interface PlacesRepository {

    Maybe<List<Pair<String, String>>> predictPlaceNames(String query);

    Maybe<Pair<Double, Double>> seeLatLng(String placeId);
}