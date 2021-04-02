package ua.in.khol.oleh.touristweathercomparer.model.places;

import android.content.Context;
import android.util.Pair;

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

import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.maps.GeocodingAuth;

public class RxPlacesHelper implements PlacesHelper {

    private final PlacesClient mPlacesClient;
    private final AutocompleteSessionToken mToken;

    public RxPlacesHelper(Context context) {
        // TODO[38] replace with PlacesApiKey
        String placesApiKey = PlacesAuth.getPlacesApiKey();
        if (!Places.isInitialized())
            Places.initialize(context, placesApiKey);

        // Create a new Places client instance.
        mPlacesClient = Places.createClient(context);

        // Create a new token for the autocomplete session.
        // Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        mToken = AutocompleteSessionToken.newInstance();
    }

    @Override
    public Single<List<Pair<String, String>>> seePlacesList(String query) {
        return Single.create(emitter -> {
            List<Pair<String, String>> list = new ArrayList<>();

            FindAutocompletePredictionsRequest request
                    = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.GEOCODE)
                    .setSessionToken(mToken)
                    .setQuery(query)
                    .build();
            mPlacesClient.findAutocompletePredictions(request)
                    .addOnSuccessListener(response -> {
                        List<AutocompletePrediction> predictions
                                = response.getAutocompletePredictions();
                        for (AutocompletePrediction prediction : predictions) {
                            String id = prediction.getPlaceId();
                            String name = prediction.getPrimaryText(null).toString()
                                    + "," + prediction.getSecondaryText(null);
                            list.add(new Pair<>(id, name));
                        }

                        emitter.onSuccess(list);
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                    });
        });
    }

    @Override
    public Single<LatLon> seeLatlon(String placeId) {
        return Single.create(emitter -> {
            // Specify the fields to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG);

            // Construct a request object, passing the place ID and fields array.
            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                    .setSessionToken(mToken)
                    .build();

            // Add a listener to handle the response.
            mPlacesClient.fetchPlace(request)
                    .addOnSuccessListener(response -> {
                        Place place = response.getPlace();
                        LatLng latLng = place.getLatLng();
                        LatLon latLon = new LatLon(latLng.latitude, latLng.longitude);

                        emitter.onSuccess(latLon);
                    });
        });
    }
}
