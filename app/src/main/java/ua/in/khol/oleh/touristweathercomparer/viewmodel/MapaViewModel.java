package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.lifecycle.MutableLiveData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Place;

public class MapaViewModel extends BaseViewModel {

    private final MutableLiveData<Place> mPlace = new MutableLiveData<>();

    public MapaViewModel(Repository repository) {
        super(repository);

        subscribePlace();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    private void subscribePlace() {
        getCompositeDisposable().add(getRepository().observePlace()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mPlace::setValue));
    }

    public MutableLiveData<Place> getPlace() {
        return mPlace;
    }

}
