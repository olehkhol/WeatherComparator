package ua.in.khol.oleh.touristweathercomparer.ui.rx;

import io.reactivex.Observable;
import io.reactivex.Observer;

public abstract class InitialValueObservable<T> extends Observable<T> {

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        subscribeListener(observer);
        observer.onNext(getInitialValue());
    }

    protected abstract void subscribeListener(Observer<? super T> observer);

    protected abstract T getInitialValue();
}
