package ua.in.khol.oleh.touristweathercomparer.model.location.google;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.location.RxLocation;

public class RxGoogleLocation implements RxLocation {
    private static final String TAG = RxGoogleLocation.class.getName();

    private final FusedLocationProviderClient mFusedLocationProviderClient;

    public RxGoogleLocation(Context context) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public Single<LatLon> observeSingleLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(0);
        locationRequest.setMaxWaitTime(1000);
        locationRequest.setNumUpdates(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return Single.create(emitter -> mFusedLocationProviderClient
                .requestLocationUpdates(locationRequest,
                        new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                List<Location> locations = locationResult.getLocations();
                                Location location = locations.get(locations.size() - 1);
                                double lat = location.getLatitude();
                                double lon = location.getLongitude();
                                Log.d(TAG,
                                        String.format("Latitude:%f, longitude:%f", lat, lon));
                                emitter.onSuccess(new LatLon(lat, lon));
                                mFusedLocationProviderClient.removeLocationUpdates(this);
                            }

                            @Override
                            public void onLocationAvailability(LocationAvailability availability) {
                            }
                        }, null));
    }

    @Override
    public Observable<Boolean> observeUsability() {
        LocationRequest locationRequest = LocationRequest.create();

        return Observable.create(emitter -> mFusedLocationProviderClient
                .requestLocationUpdates(locationRequest,
                        new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                            }

                            @Override
                            public void onLocationAvailability(LocationAvailability availability) {
                                boolean usable = availability.isLocationAvailable();
                                Log.d(TAG,
                                        String.format("Availability is %b", usable));
                                emitter.onNext(usable);
                            }
                        }, null));
    }
}
