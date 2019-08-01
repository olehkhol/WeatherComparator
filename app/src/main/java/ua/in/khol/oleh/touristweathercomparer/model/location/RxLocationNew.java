package ua.in.khol.oleh.touristweathercomparer.model.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;


public class RxLocationNew implements RxLocation {

    private LocationCallback mLocationCallback;
    @SuppressLint("StaticFieldLeak") // Use an Application Context
    private FusedLocationProviderClient mFusedLocationClient;
    private Observable<Location> mLocationObservable;
    private ConnectableObservable<Location> sPublished;

    public RxLocationNew(Context context) {
        if (mFusedLocationClient == null)
            mFusedLocationClient = LocationServices
                    .getFusedLocationProviderClient(context.getApplicationContext());

    }

    @SuppressLint("MissingPermission")
    public Observable<Location> getLocationObservable(int accuracy, int power,
                                                      int time) {
        if (mLocationObservable == null) {
            mLocationObservable = Observable.create(emitter -> {
                if (mLocationCallback == null) {
                    mLocationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            if (locationResult != null)
                                emitter.onNext(locationResult.getLastLocation());
                        }
                    };
                    update(accuracy, power, time);
                }
            });
            sPublished = mLocationObservable.publish();
        }


        return sPublished;
    }

    @SuppressLint("MissingPermission")
    public void update(int accuracy, int power, int time) {

        if (mLocationCallback != null) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(time);
            int priority = LocationRequest.PRIORITY_NO_POWER;
            switch (power) {
                case 1:
                    priority = LocationRequest.PRIORITY_LOW_POWER;
                    break;
                case 2:
                    priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                    break;
                case 3:
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
                    break;
            }
            locationRequest.setPriority(priority);

            mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback,
                    null /* Looper */);
        }
    }
}
