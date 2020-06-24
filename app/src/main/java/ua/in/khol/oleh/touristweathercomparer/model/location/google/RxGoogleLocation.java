package ua.in.khol.oleh.touristweathercomparer.model.location.google;

import android.annotation.SuppressLint;
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
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.location.RxLocation;

public class RxGoogleLocation implements RxLocation {
    private static final String TAG = RxGoogleLocation.class.getName();

    private final FusedLocationProviderClient mFusedLocationProviderClient;
    private final LocationRequest mLocationRequest;

    public RxGoogleLocation(Context context) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setMaxWaitTime(1000);
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    @Override
    public Single<LatLon> seeLocation() {
        return Single.create(new SingleOnSubscribe<LatLon>() {
            @Override
            public void subscribe(SingleEmitter<LatLon> emitter) throws Exception {
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
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
                        }, null);
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public Observable<Boolean> observeUsability() {
        return Observable.create(emitter -> mFusedLocationProviderClient
                .requestLocationUpdates(mLocationRequest,
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
                                emitter.onComplete();
                            }
                        }, null));
    }
}
