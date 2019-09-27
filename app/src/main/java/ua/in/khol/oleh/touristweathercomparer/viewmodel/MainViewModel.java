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
    private final MutableLiveData<Boolean> mIsRecreate = new MutableLiveData<>();

    public MainViewModel(Repository repository) {
        super(repository);

        subscribeRecreate();
        subscribeCity();
        subscribeTitle();
        subscribeProvider();
        subscribeForecast();
    }

    @Override
    public void wakeUp() {
        getRepository().updateConfiguration();
    }

    public void processData() {
        if (!isRefreshed())
            if (!getIsRefreshing().get())
                getRepository().update();
    }

    private void subscribeRecreate() {
        getCompositeDisposable()
                .add(getRepository().observeStatus().subscribe(value -> {
                    switch (value) {
                        case OFFLINE:
                        case ONLINE:
                            break;
                        case ERROR:
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
                            mIsRecreate.setValue(true);
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
                    }
                    mProviders.get(providerId).putForecast(forecast);
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

    public MutableLiveData<Boolean> getIsRecreate() {
        return mIsRecreate;
    }

    public void setIsRecreate(Boolean isRecreate) {
        mIsRecreate.setValue(isRecreate);
    }
}
