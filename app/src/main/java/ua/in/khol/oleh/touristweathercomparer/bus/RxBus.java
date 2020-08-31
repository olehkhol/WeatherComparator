package ua.in.khol.oleh.touristweathercomparer.bus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {
    private final PublishSubject<Object> mBus = PublishSubject.create();

    public void send(Object o) {
        mBus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }
}
