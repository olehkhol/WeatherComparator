package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableField;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.events.SingleLiveEvent;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;

public class MainViewModel extends BaseViewModel {
    // Data Binding
    private final ObservableField<City> mCity = new ObservableField<>();
    private final ObservableField<Settings> mSettings = new ObservableField<>();

    // Events
    private final SingleLiveEvent<Boolean> mAskForLocation = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> mDoRecreate = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> mAskForInternet = new SingleLiveEvent<>();

    // Test
    private boolean mRefreshed = false;

    public MainViewModel(Repository repository) {
        super(repository);
        // TODO[39] try to replace this trick with Splash
        mSettings.set(repository.getSettings());
        subscribeOnStatus();
        subscribeCity();
    }

    @Override
    public void refresh() {
        if (!mRefreshed)
            getRepository().getRefreshSubject().onNext(true);
        else
            getRepository().getRefreshSubject().onNext(false);
    }

    // TODO remove the code with clear status somewhere according to the view state
    private void subscribeOnStatus() {
        getCompositeDisposable().add(getRepository().observeStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {
                    switch (status) {
                        case OFFLINE:
                            mAskForInternet.setValue(false);
                            break;
                        case CRITICAL_OFFLINE:
                            mAskForInternet.setValue(true);
                            break;
                        case LOCATION_UNAVAILABLE:
                            mAskForLocation.setValue(true);
                            break;
                        case NEED_RECREATE:
                        default:
                            mDoRecreate.setValue(true);
                            break;
                    }
                }));
    }

    private void subscribeCity() {
        getCompositeDisposable().add(getRepository().observeCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    mCity.set(value);
                    mRefreshed = true;
                }));
    }

    public ObservableField<City> getCity() {
        return mCity;
    }

    public ObservableField<Settings> getSettings() {
        return mSettings;
    }

    public SingleLiveEvent<Boolean> getAskForLocation() {
        return mAskForLocation;
    }

    public SingleLiveEvent<Boolean> getDoRecreate() {
        return mDoRecreate;
    }

    public SingleLiveEvent<Boolean> getAskForInternet() {
        return mAskForInternet;
    }
}
