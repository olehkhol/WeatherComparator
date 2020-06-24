package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;

public class ForecastViewModel extends BaseViewModel {

    private final Settings mSettings;
    private final ObservableList<Average> mAverages = new ObservableArrayList<>();
    private final ObservableField<Average> mCurrent = new ObservableField<>();
    public final ObservableBoolean mRefreshing = new ObservableBoolean(false);
    public final ObservableBoolean mRefreshed = new ObservableBoolean(false);
    private final ObservableField<Boolean> mPermissions = new ObservableField<>();
    private int mActiveRefreshing = 0;

    public ForecastViewModel(Repository repository) {
        super(repository);

        mSettings = repository.getSettings();
        mPermissions.set(repository.getPermissions());
        subscribeCurrent();
        subscribeAverages();
        refresh();
    }

    @Override
    public void refresh() {
        setActiveRefreshing();
        getRepository().processRefresh(false);
    }

    public void onSwipe() {
        refresh();
    }

    private void subscribeCurrent() {
        getCompositeDisposable().add(getRepository().observeCurrent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    mCurrent.set(value);
                    decActiveRefreshing();
                })
        );
    }

    private void subscribeAverages() {
        getCompositeDisposable().add(getRepository().observeAverages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(averages -> {
                    mAverages.clear();
                    mAverages.addAll(averages);
                    decActiveRefreshing();
                })
        );
    }

    private void setActiveRefreshing() {
        mActiveRefreshing = getCompositeDisposable().size();
        mRefreshing.set(true);
        mRefreshed.set(false);
    }

    private void decActiveRefreshing() {
        mActiveRefreshing--;
        if (mActiveRefreshing == 0) {
            mRefreshing.set(false);
            mRefreshed.set(true);
        }
    }

    public Settings getSettings() {
        return mSettings;
    }

    public ObservableField<Average> getCurrent() {
        return mCurrent;
    }

    public ObservableList<Average> getAverages() {
        return mAverages;
    }
}
