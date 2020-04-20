package ua.in.khol.oleh.touristweathercomparer.model.net;

import io.reactivex.Observable;

public interface RxNetwork {
    Observable<Boolean> observeInternetConnectivity();
}
