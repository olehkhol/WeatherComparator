package ua.in.khol.oleh.touristweathercomparer.model.location;

import android.location.Location;

import io.reactivex.Observable;

interface RxLocation {
    Observable<Location> getLocationObservable(int accuracy, int power, int time);

    void update(int accuracy, int power, int time);
}
