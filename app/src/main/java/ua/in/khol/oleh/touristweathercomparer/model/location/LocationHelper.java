package ua.in.khol.oleh.touristweathercomparer.model.location;

import android.location.Location;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface LocationHelper {

    Single<Location> getSingleLocation(int accuracy, int power);

    Observable<String> getLocationName(double latitude, double longitude, String language);
}
