package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.MaybeSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.views.callbacks.MapaCallbacks;

public class MapaViewModel extends BaseViewModel implements MapaCallbacks {

    private final Settings mSettings;
    private final MutableLiveData<City> mCity = new MutableLiveData<>();
    private final MutableLiveData<List<Average>> mAverages = new MutableLiveData<>();

    public MapaViewModel(Repository repository) {
        super(repository);

        mSettings = repository.getSettings();

        subscribeOnPlace();
    }

    private void subscribeOnPlace() {
        getCompositeDisposable().add(getRepository()
                .observeLatestPlace()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(place -> mCity.setValue(new City(place.getName(),
                        place.getLatitude(), place.getLongitude())))
                .observeOn(Schedulers.io())
                .flatMapMaybe((Function<Place, MaybeSource<List<Average>>>) place ->
                        getRepository().tryDailies(place.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAverages::setValue));
    }

    @Override
    public void onMapaClicked(double latitude, double longitude) {
        getRepository().setLocation(new LatLon(latitude, longitude));
    }

    @Override
    public void onMapaLocationClicked(double latitude, double longitude) {
        getRepository().setLocation(new LatLon(latitude, longitude));
    }

    @Override
    public void onMapaLocationButtonClicked() {
        getRepository().coldRefresh();
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
