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

public class AveragesViewModel extends BaseViewModel {

    public final ObservableBoolean mRefreshing = new ObservableBoolean(true);
    public final ObservableBoolean mCurrentRefreshed = new ObservableBoolean(false);
    public final ObservableBoolean mDailiesRefreshed = new ObservableBoolean(false);
    private final Settings mSettings;
    private final ObservableList<Average> mDailies = new ObservableArrayList<>();
    private final ObservableField<Average> mCurrent = new ObservableField<>();
    private int mActiveUpdates = 0;
    private long mPlaceId;

    public AveragesViewModel(Repository repository) {
        super(repository);

        mSettings = repository.getSettings();
    }

    public void onSwipe() {
        subscribeCurrent();
        subscribeAverages();

        setActiveUpdates();
    }

    private void subscribeCurrent() {
        getCompositeDisposable().add(getRepository()
                .tryCurrent(mPlaceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::decActiveUpdates)
                .subscribe(average -> {
                    decActiveUpdates();
                    mCurrent.set(average);
                    mCurrentRefreshed.set(true);
                }, Throwable::printStackTrace)
        );
    }

    private void subscribeAverages() {
        getCompositeDisposable().add(getRepository()
                .tryDailies(mPlaceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::decActiveUpdates)
                .subscribe(averages -> {
                    decActiveUpdates();
                    mDailies.clear();
                    mDailies.addAll(averages);
                    mDailiesRefreshed.set(true);
                }, Throwable::printStackTrace));
    }

    private void setActiveUpdates() {
        mActiveUpdates = getCompositeDisposable().size();
        mRefreshing.set(true);
    }

    private void decActiveUpdates() {
        mActiveUpdates--;
        if (mActiveUpdates == 0) {
            mRefreshing.set(false);
            getCompositeDisposable().clear();
        }
    }

    public Settings getSettings() {
        return mSettings;
    }

    public ObservableField<Average> getCurrent() {
        return mCurrent;
    }

    public ObservableList<Average> getDailies() {
        return mDailies;
    }

    public void setPlaceId(long placeId) {
        mPlaceId = placeId;
        onSwipe();
    }
}