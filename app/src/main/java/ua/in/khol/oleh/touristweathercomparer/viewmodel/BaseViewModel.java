package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;

abstract class BaseViewModel extends ViewModel {

    private final Repository mRepository;
    private final CompositeDisposable mCompositeDisposable;

    BaseViewModel(Repository repository) {
        mRepository = repository;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!mCompositeDisposable.isDisposed())
            mCompositeDisposable.dispose();
    }

    CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    Repository getRepository() {
        return mRepository;
    }
}
