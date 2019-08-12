package ua.in.khol.oleh.touristweathercomparer.model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ua.in.khol.oleh.touristweathercomparer.helpers.LocaleUnits;
import ua.in.khol.oleh.touristweathercomparer.model.location.RxLocationCity;
import ua.in.khol.oleh.touristweathercomparer.model.location.RxLocationOld;
import ua.in.khol.oleh.touristweathercomparer.model.location.data.CityLocation;
import ua.in.khol.oleh.touristweathercomparer.model.network.RxConnection;
import ua.in.khol.oleh.touristweathercomparer.model.weather.AbstractProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.DarkSky;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.Wwo;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.Yahoo;

import static ua.in.khol.oleh.touristweathercomparer.model.Preferences.CELSIUS;
import static ua.in.khol.oleh.touristweathercomparer.model.Preferences.GPS_CHECK;
import static ua.in.khol.oleh.touristweathercomparer.model.Preferences.LANGUAGE;
import static ua.in.khol.oleh.touristweathercomparer.model.Preferences.POWER;
import static ua.in.khol.oleh.touristweathercomparer.model.Preferences.TIME;

public class Repository implements BaseRepository {
    private static final String POWER_DEFAULT = "1";
    private static final String TIME_DEFAULT = "3600";
    private static final String LANGUAGE_DEFAULT = "en";
    private final Context mAppContext;
    private SharedPreferences mSharedPreferences;
    private int mAccuracy;
    private int mPower;
    private int mTime;
    private boolean mCelsius;
    private RxLocationOld mRxLocation;
    private RxConnection mRxConnection;
    private List<AbstractProvider> mWeatherProviders = new ArrayList<AbstractProvider>() {{
        add(new Yahoo());
        add(new DarkSky());
        add(new Wwo());
    }};
    private PublishSubject<Boolean> mRefreshNeeded;

    public Repository(Application application) {
        // Global application context
        mAppContext = application.getApplicationContext();
        mRxLocation = new RxLocationOld(mAppContext);
        mRxConnection = new RxConnection(mAppContext);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mAppContext);
        mRefreshNeeded = PublishSubject.create();
        updatePreferences();
    }

    @Override
    public Observable<CityLocation> getSingleLocation() {
        return mRxLocation
                .getLocationObservable(mAccuracy, mPower, mTime)
                .take(1)
                .map(location -> new CityLocation(location.getLatitude(), location.getLongitude()))
                .doOnComplete(() -> mRxLocation.cancel());

    }


    @Override
    public Observable<String> getCityName(double latitude, double longitude) {
        return mRxConnection.getStatusObservable()
                .filter(aBoolean -> aBoolean)
                .switchMap(aBoolean -> {
                    String language = Locale.getDefault().getLanguage();
                    return RxLocationCity
                            .getCityObservable(latitude, longitude, language)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                });
    }

    @Override
    public Observable<ProviderData> getProvidersData(double latitude, double longitude) {
        return mRxConnection.getStatusObservable()
                .filter(aBoolean -> aBoolean)
                .switchMap(aBoolean -> {
                    List<Observable<ProviderData>> observables = new ArrayList<>();
                    for (AbstractProvider provider : mWeatherProviders)
                        observables
                                .add(provider.getWeatherObservable(latitude, longitude));
                    return Observable
                            .concat(observables)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                });
    }

    private Observable<Location> getLocationObservable() {
        return mRxLocation
                .getLocationObservable(mAccuracy, mPower, mTime);
    }

    @Override
    public Observable<CityLocation> getLocation() {
        return getLocationObservable()
                .map(location -> new CityLocation(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public Observable<String> getCityName() {
        return mRxConnection.getStatusObservable()
                .filter(aBoolean -> aBoolean)
                .switchMap(aBoolean -> getLocationObservable()
                        .take(1)
                        .flatMap(location -> {
                                    String language = Locale.getDefault().getLanguage();
                                    return RxLocationCity
                                            .getCityObservable(location.getLatitude(),
                                                    location.getLongitude(),
                                                    language)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread());
                                }
                        ));
    }

    @Override
    public Observable<ProviderData> getProvidersData() {
        return mRxConnection.getStatusObservable()
                .filter(aBoolean -> aBoolean)
                .switchMap(aBoolean -> getLocationObservable()
                        .take(1)// Take one to complete whole bunch of observables
                        .flatMap(location -> {
                            List<Observable<ProviderData>> observables = new ArrayList<>();
                            for (AbstractProvider provider : mWeatherProviders)
                                observables
                                        .add(provider.getWeatherObservable(location.getLatitude(),
                                                location.getLongitude()));
                            return Observable
                                    .concat(observables)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                        }))
                .doOnComplete(() -> mRxLocation.cancel());// Stop determine location
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void updatePreferences() {
        if (mSharedPreferences.contains(CELSIUS)) {
            mCelsius = mSharedPreferences.getBoolean(CELSIUS, false);
            LocaleUnits.getInstance().setCelsius(mCelsius);
        } else {
            mSharedPreferences.edit().putBoolean(CELSIUS, false).commit();
            mCelsius = false;
        }

        if (mSharedPreferences.contains(GPS_CHECK)) {
            boolean isGPS = mSharedPreferences.getBoolean(GPS_CHECK, false);
            if (isGPS)
                mAccuracy = 1;
            else
                mAccuracy = 2;
        } else {
            mSharedPreferences.edit().putBoolean(GPS_CHECK, false).commit();
            mAccuracy = 2;
        }

        if (mSharedPreferences.contains(POWER)) {
            String power = mSharedPreferences.getString(POWER, POWER_DEFAULT);
            mPower = Integer.valueOf(power);
        } else {
            mSharedPreferences.edit().putString(POWER, POWER_DEFAULT).commit();
            mPower = Integer.valueOf(POWER_DEFAULT);
        }

        if (mSharedPreferences.contains(TIME)) {
            String time = mSharedPreferences.getString(TIME, TIME_DEFAULT);
            mTime = Integer.valueOf(time) * 1000;
        } else {
            mSharedPreferences.edit().putString(TIME, TIME_DEFAULT).commit();
            mTime = Integer.valueOf(TIME_DEFAULT) * 1000;
        }

        String language = LANGUAGE_DEFAULT;
        if (mSharedPreferences.contains(LANGUAGE)) {
            language = mSharedPreferences.getString(LANGUAGE, LANGUAGE_DEFAULT);
        } else {
            mSharedPreferences.edit().putString(TIME, TIME_DEFAULT).commit();
        }
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = mAppContext.getResources().getConfiguration();
        config.locale = locale;
        mAppContext.getResources()
                .updateConfiguration(config, mAppContext.getResources().getDisplayMetrics());
    }

    public void update() {
        updatePreferences();
        mRefreshNeeded.onNext(true);
    }

    public Observable<Boolean> getRefreshObservable() {
        return mRefreshNeeded.serialize();
    }
}
