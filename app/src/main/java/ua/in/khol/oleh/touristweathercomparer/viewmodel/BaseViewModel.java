package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public abstract class BaseViewModel extends ViewModel {
    private Repository mRepository;
    private ObservableBoolean mIsRefreshing = new ObservableBoolean();
    private CompositeDisposable mCompositeDisposable;
    private boolean isRefreshed;

    public BaseViewModel(Repository repository) {
        mRepository = repository;
        mCompositeDisposable = new CompositeDisposable();
    }

    public abstract void update();

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public Repository getRepository() {
        return mRepository;
    }

    public ObservableBoolean getIsRefreshing() {
        return mIsRefreshing;
    }

    public void setIsRefreshing(Boolean isRefreshing) {
        mIsRefreshing.set(isRefreshing);
    }

    public boolean isRefreshed() {
        return isRefreshed;
    }

    public void setRefreshed(boolean refreshed) {
        isRefreshed = refreshed;
    }
}
