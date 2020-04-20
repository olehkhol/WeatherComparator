package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.views.callbacks.MapaCallbacks;

public class MapaViewModel extends BaseViewModel implements MapaCallbacks {

    private static final int DAYS_TO_DISPLAY_ON_MAP = 3;
    // Fields to be observed in View
    private final Settings mSettings;
    private final MutableLiveData<City> mCity = new MutableLiveData<>();
    private final MutableLiveData<List<Average>> mAverages = new MutableLiveData<>();

    public MapaViewModel(Repository repository) {
        super(repository);

        mSettings = repository.getSettings();
        subscribeCity();
        subscribeAverages();
        refresh();
    }

    @Override
    public void refresh() {
        getRepository().getRefreshSubject().onNext(false);
    }

    private void subscribeCity() {
        getCompositeDisposable().add(getRepository().observeCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCity::setValue));
    }

    private void subscribeAverages() {
        getCompositeDisposable().add(getRepository().observeAverages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Average>>() {
                    @Override
                    public void accept(List<Average> value) throws Exception {
                        mAverages.setValue(value.subList(0, DAYS_TO_DISPLAY_ON_MAP));
                    }
                }));
    }

    @Override
    public void onMapaClicked(double latitude, double longitude) {
        getRepository().getLatLonSubject()
                .onNext(new LatLon(latitude, longitude));
    }

    @Override
    public void onMapaLocationClicked(double latitude, double longitude) {
        getRepository().getLatLonSubject()
                .onNext(new LatLon(latitude, longitude));
    }

    @Override
    public void onMapaLocationButtonClicked() {
        getRepository().getRefreshSubject().onNext(true);
    }

    public MutableLiveData<City> getCity() {
        return mCity;
    }

    public MutableLiveData<List<Average>> getAverages() {
        return mAverages;
    }

    public Settings getSettings() {
        return mSettings;
    }
}
