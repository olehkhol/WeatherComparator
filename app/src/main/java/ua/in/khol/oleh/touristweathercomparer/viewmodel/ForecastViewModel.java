package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Town;

public class ForecastViewModel extends BaseViewModel {
    private static final int PLACES_COUNT = 3;

    private final MutableLiveData<List<Town>> mTowns = new MutableLiveData<>();

    ForecastViewModel(Repository repository) {
        super(repository);

        subscribeOnPlaces();
    }

    private void subscribeOnPlaces() {
        getCompositeDisposable().add(getRepository().observeLatestPlaces(PLACES_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(places -> {
                    List<Town> towns = new ArrayList<>();
                    for (Place place : places)
                        towns.add(new Town(place.getName(), place.getId(),
                                place.getLatitude(), place.getLongitude()));
                    mTowns.setValue(towns);
                }));
    }

    public MutableLiveData<List<Town>> getTowns() {
        return mTowns;
    }

}
