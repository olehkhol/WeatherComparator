package ua.in.khol.oleh.touristweathercomparer.viewmodel.events;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private static final String TAG = SingleLiveEvent.class.getName();
    private static final String WARNING = TAG + " %s";
    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    public void observe(@NonNull LifecycleOwner owner,
                        @NonNull final Observer<? super T> observer) {
        if (hasActiveObservers())
            System.out.println(String.format(WARNING, "Multiple observers registered."));

        // observe the internal MutableLiveData
        super.observe(owner, t -> {
            if (mPending.compareAndSet(true, false))
                observer.onChanged(t);
        });
    }

    @MainThread
    public void call() {
        setValue(null);
    }

    @MainThread
    public void setValue(@Nullable T t) {
        mPending.set(true);
        super.setValue(t);
    }
}
