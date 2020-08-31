package ua.in.khol.oleh.touristweathercomparer.model.location;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface RxLocation {
    Single<LatLon> seeLocation();

    Observable<Boolean> observeUsability();

    Maybe<LatLon> tryLatLon();
}
