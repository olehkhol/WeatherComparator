package ua.in.khol.oleh.touristweathercomparer.model.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class RxLocationOld implements RxLocation {
    private static final int MIN_DISTANCE = 0;
    private Observable<Location> mLocationObservable;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    public RxLocationOld(Context context) {
        mLocationManager = (LocationManager) context.getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public Observable<Location> getLocationObservable(int accuracy, int power,
                                                      int time) {
        if (mLocationObservable == null) {
            mLocationObservable = Observable
                    .create((ObservableOnSubscribe<Location>) emitter -> {
                        if (mLocationListener == null) {
                            mLocationListener = new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    emitter.onNext(location);
                                    Log.d("RxLocationOld", location.toString());
                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle b) {
                                }

                                @Override
                                public void onProviderEnabled(String provider) {
                                }

                                @Override
                                public void onProviderDisabled(String provider) {
                                }
                            };
                            update(accuracy, power, time);
                        }
                    })
                    // .just(createNewLocation(50.4547, 30.5238))
                    .doOnSubscribe(disposable -> update(accuracy, power, time))
                    .doOnDispose(this::cancel)
                    .publish().refCount();
        }

        return mLocationObservable;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void update(int accuracy, int power, int time) {
        if (mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
            Criteria criteria = getCriteria(accuracy, power);
            mLocationManager.requestSingleUpdate(criteria, mLocationListener, null);
            mLocationManager.requestLocationUpdates(time, MIN_DISTANCE,
                    criteria, mLocationListener, null);
        }
    }

    public void cancel() {
        if (mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationListener = null;
            mLocationObservable = null;
        }
    }

    private Criteria getCriteria(int accuracy, int power) {
        Criteria criteria = new Criteria();
        int accuracyCriteria;
        switch (accuracy) {
            case 1:
                accuracyCriteria = Criteria.ACCURACY_FINE;
                break;
            case 2:
            default:
                accuracyCriteria = Criteria.ACCURACY_COARSE;
                break;
        }
        criteria.setAccuracy(accuracyCriteria);
        int powerCriteria;
        switch (power) {
            case 1:
            default:
                powerCriteria = Criteria.POWER_LOW;
                break;
            case 2:
                powerCriteria = Criteria.POWER_MEDIUM;
                break;
            case 3:
                powerCriteria = Criteria.POWER_HIGH;
                break;

        }
        criteria.setPowerRequirement(powerCriteria);

        return criteria;
    }

    private Location createNewLocation(double latitude, double longitude) {
        Location location = new Location("?");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }

}
