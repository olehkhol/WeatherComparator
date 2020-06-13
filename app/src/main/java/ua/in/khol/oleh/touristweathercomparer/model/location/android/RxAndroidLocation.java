package ua.in.khol.oleh.touristweathercomparer.model.location.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.location.RxLocation;

public class RxAndroidLocation implements RxLocation {
    private static final String TAG = RxAndroidLocation.class.getName();

    private final LocationManager mLocationManager;
    private Criteria mCriteria;

    public RxAndroidLocation(Context context) {
        mLocationManager = (LocationManager) context.getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        mCriteria = getCriteria(Criteria.ACCURACY_FINE, Criteria.POWER_HIGH);
    }

    @SuppressLint("MissingPermission")
    @Override
    public Single<LatLon> observeSingleLocation() {
        return Single.create(new SingleOnSubscribe<LatLon>() {
            @Override
            public void subscribe(SingleEmitter<LatLon> emitter) throws Exception {
                mLocationManager
                        .requestSingleUpdate(mCriteria,
                                new LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {
                                        double lat = location.getLatitude();
                                        double lon = location.getLongitude();
                                        Log.d(TAG, String.format("Latitude:%f, longitude:%f", lat, lon));
                                        emitter.onSuccess(new LatLon(lat, lon));
                                        mLocationManager.removeUpdates(this);
                                    }

                                    @Override
                                    public void onStatusChanged(String provider,
                                                                int status, Bundle extras) {
                                    }

                                    @Override
                                    public void onProviderEnabled(String provider) {
                                    }

                                    @Override
                                    public void onProviderDisabled(String provider) {
                                    }
                                }, null);
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public Observable<Boolean> observeUsability() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(isLocationEnabled());
                emitter.onComplete();
            }
        });
    }

    private boolean isLocationEnabled() {
        boolean gps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean net = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean locationEnabled = gps | net;

        Log.d(TAG, String.format("Location enabled:%b", locationEnabled));

        return locationEnabled;
    }

    private Criteria getCriteria(int accuracy, int power) {
        Criteria criteria = new Criteria();

        criteria.setAccuracy(accuracy);
        criteria.setPowerRequirement(power);

        return criteria;
    }
}
