package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;

public class MarkerViewModel extends BaseViewModel {

    // Fields to be observed in View
    private final Settings mSettings;
    private final List<Average> mCurrents = new ArrayList<>();
    private final MutableLiveData<City> mCity = new MutableLiveData<>();
    private final MutableLiveData<Average> mCurrent = new MutableLiveData<>();
    private final MutableLiveData<Average> mAverages = new MutableLiveData<>();

    public MarkerViewModel(Repository repository) {
        super(repository);

        mSettings = repository.getSettings();
        subscribeCity();
        subscribeCurrent();
        refresh();
    }

    @Override
    public void refresh() {
        getRepository().processRefresh(false);
    }

    private void subscribeCity() {
        getCompositeDisposable().add(getRepository().observeCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCity::setValue));
    }

    private void subscribeCurrent() {
        getCompositeDisposable().add(getRepository().observeCurrent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(current -> {
                    mCurrents.add(current);
                    mCurrent.setValue(current);
                }));
    }

    public MutableLiveData<City> getCity() {
        return mCity;
    }

    public MutableLiveData<Average> getCurrent() {
        return mCurrent;
    }

    public Settings getSettings() {
        return mSettings;
    }

    public List<Average> getCurrents() {
        return mCurrents;
    }
}
