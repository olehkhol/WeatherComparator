package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Place;

public class MainViewModel extends BaseViewModel {
    // Fields for Data Binding
    private final ObservableField<Place> mPlace = new ObservableField<>();
    private final ObservableField<City> mCity = new ObservableField<>();

    // Fields to be observed in View
    private final MutableLiveData<Boolean> mDoRecreate = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mAskForInternetSoftly = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mAskForInternet = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mAskForLocation = new MutableLiveData<>();

    public MainViewModel(Repository repository) {
        super(repository);

        subscribeOnStatus();
        subscribeCity();
    }

    @Override
    public void start() {
        getRepository().updateConfiguration();
    }

    @Override
    public void stop() {

    }

    public void processData() {
        if (!isRefreshed())
            if (!getIsRefreshing().get())
                getRepository().update();
    }

    private void subscribeOnStatus() {
        getCompositeDisposable()
                .add(getRepository().observeStatus().subscribe(status -> {
                    switch (status) {
                        case CLEAR:
                            mAskForInternetSoftly.postValue(false);
                            mAskForInternet.postValue(false);
                            mAskForLocation.postValue(false);
                            break;
                        case OFFLINE:
                            mAskForInternetSoftly.postValue(true);
                            break;
                        case CRITICAL_OFFLINE:
                            mAskForInternet.postValue(true);
                            break;
                        case LOCATION_UNAVAILABLE:
                            mAskForLocation.postValue(true);
                            break;
                        case ONLINE:
                            break;
                        case ERROR:
                            break;
                        case REFRESHING:
                            setIsRefreshing(true);
                            setRefreshed(false);
                            break;
                        case REFRESHED:
                            setIsRefreshing(false);
                            setRefreshed(true);
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
                .subscribe(mCity::set));
    }

    public ObservableField<Place> getPlace() {
        return mPlace;
    }

    public ObservableField<City> getCity() {
        return mCity;
    }

    public MutableLiveData<Boolean> getDoRecreate() {
        return mDoRecreate;
    }

    public void setDoRecreate(Boolean doRecreate) {
        mDoRecreate.setValue(doRecreate);
    }

    public MutableLiveData<Boolean> getAskForInternetSoftly() {
        return mAskForInternetSoftly;
    }

    public void setAskForInternetSoftly(boolean ask) {
        mAskForInternetSoftly.setValue(ask);
    }

    public MutableLiveData<Boolean> getAskForInternet() {
        return mAskForInternet;
    }

    public void setAskForInternet(boolean ask) {
        mAskForInternet.setValue(ask);
    }

    public MutableLiveData<Boolean> getAskForLocation() {
        return mAskForLocation;
    }

    public void setAskForLocation(boolean ask) {
        mAskForLocation.setValue(ask);
    }
}
