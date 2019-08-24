package ua.in.khol.oleh.touristweathercomparer.model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ua.in.khol.oleh.touristweathercomparer.model.location.LocationHelper;
import ua.in.khol.oleh.touristweathercomparer.model.location.data.City;
import ua.in.khol.oleh.touristweathercomparer.model.preferences.PreferencesHelper;
import ua.in.khol.oleh.touristweathercomparer.model.weather.AbstractProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.DarkSky;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.Wwo;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.Yahoo;
import ua.in.khol.oleh.touristweathercomparer.utils.LocaleUnits;

public class GodRepository implements Repository {
    private Context mAppContext;
    private final LocationHelper mLocationHelper;
    private final PreferencesHelper mPreferencesHelper;
    private int mAccuracy;
    private int mPower;
    private List<AbstractProvider> mWeatherProviders = new ArrayList<AbstractProvider>() {{
        add(new Yahoo());
        add(new DarkSky());
        add(new Wwo());
    }};
    private PublishSubject<Boolean> mRefreshNeeded;
    private Location mCurrentLocation;

    public GodRepository(Application application,
                         LocationHelper locationHelper,
                         PreferencesHelper preferencesHelper) {
        mAppContext = application.getApplicationContext();
        mLocationHelper = locationHelper;
        mPreferencesHelper = preferencesHelper;
        mRefreshNeeded = PublishSubject.create();
        updateMetricUnits();
    }

    public Observable<Boolean> getRefreshObservable() {
        return mRefreshNeeded.serialize();
    }

    @Override
    public Observable<City> getCity() {
        return ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(aBoolean -> aBoolean)
                .take(1)
                .switchMap(aBoolean -> mLocationHelper.getSingleLocation(mAccuracy, mPower)
                        .doOnNext(location -> mCurrentLocation = location)
                        .switchMap(location -> mLocationHelper
                                .getLocationName(location.getLatitude(), location.getLongitude(),
                                        mPreferencesHelper.getLanguage())
                                .map(name -> new City(name, mCurrentLocation.getLatitude(),
                                        mCurrentLocation.getLongitude()))));
    }

    @Override
    public Observable<ProviderData> getProvidersData() {
        return ReactiveNetwork.observeInternetConnectivity()
                .filter(aBoolean -> aBoolean)
                .take(1)
                .switchMap(aBoolean -> {
                    List<Observable<ProviderData>> observables = new ArrayList<>();
                    for (AbstractProvider provider : mWeatherProviders)
                        observables
                                .add(provider.getWeatherObservable(mCurrentLocation.getLatitude(),
                                        mCurrentLocation.getLongitude()));
                    return Observable
                            .concat(observables);
                });
    }


    @Override
    public void update() {
        updateMetricUnits();
        updateConfiguration();
        mRefreshNeeded.onNext(true);
    }

    @SuppressLint("ApplySharedPref")
    private void updateMetricUnits() {
        LocaleUnits.getInstance().setCelsius(mPreferencesHelper.getCelsius());
        LocaleUnits.getInstance().setLanguage(mPreferencesHelper.getLanguage());
        mAccuracy = mPreferencesHelper.getAccuracy();
        mPower = mPreferencesHelper.getPower();
    }

    @Override
    public void updateConfiguration() {
        String language = mPreferencesHelper.getLanguage();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = mAppContext.getResources().getConfiguration();
        config.locale = locale;
        mAppContext.getResources().updateConfiguration(config,
                mAppContext.getResources().getDisplayMetrics());
    }

}
