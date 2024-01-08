package ua.in.khol.oleh.touristweathercomparer.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import ua.in.khol.oleh.touristweathercomparer.executor.JobExecutor;

public abstract class BaseViewModel extends ViewModel {

    private final Executor executor = new JobExecutor();
    private final Scheduler schedulerMainThread = AndroidSchedulers.mainThread();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected final SingleLiveEvent<ProgressEvent> progressEvent = new SingleLiveEvent<>(ProgressEvent.IDLE);

    protected synchronized <T> void execute(Observable<T> observable,
                                            DisposableObserver<T> observer) {
        addDisposable(observable
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribeWith(observer));
    }

    protected synchronized <T> void execute(Observable<T> observable,
                                            Consumer<T> consumer) {
        addDisposable(observable
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribe(consumer));
    }

    protected synchronized <T> void execute(Flowable<T> flowable,
                                            Consumer<T> consumer) {
        addDisposable(flowable
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribe(consumer));
    }

    protected synchronized <T> void execute(Flowable<T> flowable,
                                            DisposableSubscriber<T> subscriber) {
        addDisposable(flowable
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribeWith(subscriber));
    }

    protected synchronized <T> void execute(Single<T> single,
                                            DisposableSingleObserver<T> observer) {
        addDisposable(single
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribeWith(observer));
    }

    protected synchronized <T> void execute(Single<T> single,
                                            Consumer<T> consumer) {
        addDisposable(single
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribe(consumer));
    }

    protected synchronized <T> void execute(Maybe<T> maybe,
                                            DisposableMaybeObserver<T> observer) {
        addDisposable(maybe
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribeWith(observer));
    }

    protected synchronized <T> void execute(Maybe<T> maybe,
                                            Consumer<T> consumer) {
        addDisposable(maybe
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribe(consumer));
    }

    protected synchronized <T> void execute(Maybe<T> maybe,
                                            Consumer<T> consumer,
                                            Consumer<Throwable> throwable) {
        addDisposable(maybe
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribe(consumer, throwable));
    }

    protected synchronized void execute(Completable completable,
                                        DisposableCompletableObserver observer) {
        addDisposable(completable
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribeWith(observer));
    }

    protected synchronized void execute(Completable completable) {
        addDisposable(completable
                .subscribeOn(Schedulers.from(executor))
                .observeOn(schedulerMainThread)
                .subscribe());
    }

    protected void onCleared() {
        super.onCleared();
        if (!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public LiveData<ProgressEvent> getProgressEvent() {
        return progressEvent;
    }
}