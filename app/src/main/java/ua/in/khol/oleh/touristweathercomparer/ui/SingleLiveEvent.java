package ua.in.khol.oleh.touristweathercomparer.ui;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private final AtomicBoolean pending = new AtomicBoolean(false);

    public SingleLiveEvent(T value) {
        super(value);
    }

    @MainThread
    public void observe(@NonNull LifecycleOwner owner,
                        @NonNull final Observer<? super T> observer) {
        super.observe(owner, value -> {
            if (pending.compareAndSet(true, false))
                observer.onChanged(value);
        });
    }

    @MainThread
    public void setValue(@Nullable T t) {
        pending.set(true);
        super.setValue(t);
    }
}