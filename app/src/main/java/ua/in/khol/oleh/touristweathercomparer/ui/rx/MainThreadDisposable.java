package ua.in.khol.oleh.touristweathercomparer.ui.rx;

import android.os.Looper;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public abstract class MainThreadDisposable implements Disposable {

    private final AtomicBoolean unsubscribed = new AtomicBoolean();

    @Override
    public boolean isDisposed() {
        return unsubscribed.get();
    }

    @Override
    public void dispose() {
        if (unsubscribed.compareAndSet(false, true)) {
            if (Looper.myLooper() == Looper.getMainLooper())
                onDispose();
            else
                AndroidSchedulers.mainThread().scheduleDirect(this::onDispose);
        }
    }

    protected abstract void onDispose();
}
