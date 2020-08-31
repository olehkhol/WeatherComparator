package ua.in.khol.oleh.touristweathercomparer.model.location.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.location.RxLocation;

public class RxAndroidLocation implements RxLocation {
    private final LocationManager mLocationManager;
    private final Criteria mCriteria;

    public RxAndroidLocation(Context context) {
        mLocationManager = (LocationManager) context.getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        mCriteria = getCriteria(Criteria.ACCURACY_FINE, Criteria.POWER_HIGH);
    }

    @SuppressLint("MissingPermission")
    @Override
    public Single<LatLon> seeLocation() {
        return Single.create(new SingleOnSubscribe<LatLon>() {
            @Override
            public void subscribe(SingleEmitter<LatLon> emitter) throws Exception {
                mLocationManager
                        .requestSingleUpdate(mCriteria,
                                new LocationListener() {
                                    @Override
                                    public void onLocationChanged(@NotNull Location location) {
                                        double lat = location.getLatitude();
                                        double lon = location.getLongitude();

                                        emitter.onSuccess(new LatLon(lat, lon));
                                        mLocationManager.removeUpdates(this);
                                    }

                                    @Override
                                    public void onStatusChanged(String provider,
                                                                int status, Bundle extras) {
                                    }

                                    @Override
                                    public void onProviderEnabled(@NotNull String provider) {
                                    }

                                    @Override
                                    public void onProviderDisabled(@NotNull String provider) {
                                    }
                                }, null);
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public Observable<Boolean> observeUsability() {
        return Observable.create(emitter -> {
            boolean enabled = isLocationEnabled();
            emitter.onNext(enabled);
            if (enabled)
                emitter.onComplete();
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public Maybe<LatLon> tryLatLon() {
        return Maybe.fromCallable(() -> {
            if (!isLocationEnabled())
                return null;

            Location location = mLocationManager
                    .getLastKnownLocation(mLocationManager.getBestProvider(mCriteria, true));
            if (location != null)
                return new LatLon(location.getLatitude(), location.getLongitude());

            return null;
        });
    }

    private boolean isLocationEnabled() {
        boolean gps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean net = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gps | net;
    }

    private Criteria getCriteria(int accuracy, int power) {
        Criteria criteria = new Criteria();

        criteria.setAccuracy(accuracy);
        criteria.setPowerRequirement(power);

        return criteria;
    }
}
