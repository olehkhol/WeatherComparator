package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.functions.Consumer;
import ua.in.khol.oleh.touristweathercomparer.model.GodRepository;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Location;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public class MainViewModel extends BaseViewModel {
    private final ObservableField<City> mCity = new ObservableField<>();
    private final ObservableField<Location> mLocation = new ObservableField<>();
    private final ObservableList<Title> mTitles = new ObservableArrayList<>();
    private final ObservableList<Provider> mProviders = new ObservableArrayList<>();
    private final MutableLiveData<Boolean> mIsRecreate = new MutableLiveData<>();

    public MainViewModel(Repository repository) {
        super(repository);

        subscribeRecreate();
        subscribeLocation();
        subscribeCity();
        subscribeTitle();
        subscribeProvider();
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
                .add(getRepository().getRefreshObservable().subscribe(new Consumer<GodRepository.Status>() {
                    @Override
                    public void accept(GodRepository.Status value) throws Exception {
                        switch (value) {
                            case NEED_CONNECTION:
                                break;
                            case CONNECTED:
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
                    }
                }));
    }

    private void subscribeLocation() {
        getCompositeDisposable().add(getRepository().getLocation().subscribe(mLocation::set));
    }

    private void subscribeCity() {
        getCompositeDisposable().add(getRepository().getCity().subscribe(mCity::set));
    }

    private void subscribeTitle() {
        getCompositeDisposable().add(getRepository().getTitle()
                .subscribe(title -> {
                    int index = mTitles.indexOf(title);
                    if (index != -1)
                        mTitles.set(index, title);// Replace if existed
                    else
                        mTitles.add(title);
                }));
    }

    private void subscribeProvider() {
        getCompositeDisposable().add(getRepository().getProvider().subscribe(mProviders::add));
    }

    public ObservableField<Location> getLocation() {
        return mLocation;
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
