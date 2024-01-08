package ua.in.khol.oleh.touristweathercomparer.data.places;

import static ua.in.khol.oleh.touristweathercomparer.Secrets.PLACES_API_KEY;

import android.content.Context;

import androidx.core.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Maybe;

public class PlacesDataRepository implements PlacesRepository {

    private final PlacesClient placesClient;
    private final AutocompleteSessionToken token;

    public PlacesDataRepository(Context context) {
        if (!Places.isInitialized())
            Places.initialize(context, PLACES_API_KEY);

        // Create a new Places client instance.
        placesClient = Places.createClient(context);

        // Create a new token for the autocomplete session.
        // Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        token = AutocompleteSessionToken.newInstance();
    }

    @Override
    public Maybe<List<Pair<String, String>>> predictPlaceNames(final String query) {
        return Maybe.create(emitter -> {
            final List<Pair<String, String>> list = new ArrayList<>();

            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest
                    .builder()
                    .setTypeFilter(TypeFilter.GEOCODE)
                    .setSessionToken(token)
                    .setQuery(query)
                    .build();
            placesClient
                    .findAutocompletePredictions(request)
                    .addOnSuccessListener(response -> {
                        List<AutocompletePrediction> predictions
                                = response.getAutocompletePredictions();

                        for (AutocompletePrediction prediction : predictions) {
                            String id = prediction.getPlaceId();
                            String name = prediction.getFullText(null).toString();
                            list.add(Pair.create(id, name));
                        }

                        emitter.onSuccess(list);
                    })
                    .addOnFailureListener(e -> emitter.onComplete());
        });
    }

    @Override
    public Maybe<Pair<Double, Double>> seeLatLng(final String placeId) {
        return Maybe.create(emitter -> {
            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG);
            FetchPlaceRequest request = FetchPlaceRequest
                    .builder(placeId, placeFields)
                    .setSessionToken(token)
                    .build();
            placesClient
                    .fetchPlace(request)
                    .addOnSuccessListener(response -> {
                        Place place = response.getPlace();
                        LatLng latLng = place.getLatLng();

                        if (latLng != null)
                            emitter.onSuccess(Pair.create(latLng.latitude, latLng.longitude));
                        else
                            emitter.onComplete();
                    });
        });
    }
}