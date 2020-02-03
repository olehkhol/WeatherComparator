package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public class HomeViewModel extends BaseViewModel {

    private final ObservableList<Title> mTitles = new ObservableArrayList<>();
    private final ObservableList<Provider> mProviders = new ObservableArrayList<>();

    public HomeViewModel(Repository repository) {
        super(repository);
        initTitles();
        initProviders();
    }

    @Override
    public void start() {
        subscribeForecast();
    }

    @Override
    public void stop() {

    }

    private void initTitles() {
        mTitles.addAll(getRepository().getTitleList());
    }

    private void initProviders() {
        mProviders.addAll(getRepository().getProviderList());
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

    public ObservableList<Title> getTitles() {
        return mTitles;
    }

    public ObservableList<Provider> getProviders() {
        return mProviders;
    }
}
