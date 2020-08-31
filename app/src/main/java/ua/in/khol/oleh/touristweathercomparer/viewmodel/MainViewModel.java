package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.bus.Event;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.events.SingleLiveEvent;

public class MainViewModel extends BaseViewModel {

    private final Settings mSettings;

    private final SingleLiveEvent<Boolean> mPermissions = new SingleLiveEvent<>(false);
    private final SingleLiveEvent<Boolean> mAskForLocation = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> mDoRecreate = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> mAskForInternet = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> mAskForPlace = new SingleLiveEvent<>();

    public MainViewModel(Repository repository) {
        super(repository);

        mSettings = repository.getSettings();
        subscribeOnBus();
    }

    private void subscribeOnBus() {
        getCompositeDisposable().add(getRepository().observeBus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof Event)
                        switch ((Event) o) {
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
                                mDoRecreate.setValue(true);
                                break;
                            case NO_PLACE:
                                mAskForPlace.setValue(true);
                                break;
                            default:
                                break;
                        }
                }));
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

    public SingleLiveEvent<Boolean> getAskForPlace() {
        return mAskForPlace;
    }

    public SingleLiveEvent<Boolean> getPermissions() {
        return mPermissions;
    }

    public int getLanguageIndex() {
        return mSettings.getLanguageIndex();
    }
}
