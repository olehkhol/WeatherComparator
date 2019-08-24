package ua.in.khol.oleh.touristweathercomparer.model.location;

import android.location.Location;

import io.reactivex.Observable;

public interface LocationHelper {

    Observable<Location> getSingleLocation(int accuracy, int power);

    Observable<String> getLocationName(double latitude, double longitude, String language);
}
