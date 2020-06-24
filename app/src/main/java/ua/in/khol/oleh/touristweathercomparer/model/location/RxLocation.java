package ua.in.khol.oleh.touristweathercomparer.model.location;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface RxLocation {
    Single<LatLon> seeLocation();

    // TODO change Observable to Single
    Observable<Boolean> observeUsability();
}
