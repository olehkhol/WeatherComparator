package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Place;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public class MainViewModel extends BaseViewModel {
    private final ObservableField<Place> mPlace = new ObservableField<>();
    private final ObservableField<City> mCity = new ObservableField<>();
    private final ObservableList<Title> mTitles = new ObservableArrayList<>();
    private final ObservableList<Provider> mProviders = new ObservableArrayList<>();
    private final MutableLiveData<Boolean> mDoRecreate = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mAskForInternetSoftly = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mAskForInternet = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mAskForLocation = new MutableLiveData<>();

    public MainViewModel(Repository repository) {
        super(repository);

        subscribeOnStatus();
        subscribeCity();
        subscribeTitle();
        subscribeProvider();
        subscribeForecast();
    }

    @Override
    public void update() {
        getRepository().updateConfiguration();
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

    private void subscribeTitle() {
        getCompositeDisposable().add(getRepository().observeTitle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(title -> {
                    int index = mTitles.indexOf(title);
                    if (index != -1)
                        mTitles.set(index, title);
                    else
                        mTitles.add(title);
                }));
    }

    private void subscribeProvider() {
        getCompositeDisposable().add(getRepository().observeProvider()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(provider -> {
                    int index = mProviders.indexOf(provider);
                    if (index != -1)
                        mProviders.set(index, provider);
                    else
                        mProviders.add(provider);
                }));
    }

    private void subscribeForecast() {
        getCompositeDisposable().add(getRepository().observeForecast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecast -> {
                    int providerId = forecast.getProviderId();
                    if (forecast.isCurrent()) {
                        mTitles.get(providerId).setCurrent(forecast.getCurrent());
                        mTitles.get(providerId).setText(forecast.getCurrentText());
                        mTitles.get(providerId).setImage(forecast.getCurrentImage());
                        mTitles.get(providerId).setVisible(true);
                    }
                    mProviders.get(providerId).putForecast(forecast);
                    mProviders.get(providerId).setVisible(true);
                }));
    }

    public ObservableField<Place> getPlace() {
        return mPlace;
    }

    public ObservableField<City> getCity() {
        return mCity;
    }

    public ObservableList<Title> getTitles() {
        return mTitles;
    }

    public ObservableList<Provider> getProviders() {
        return mProviders;
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
