package ua.in.khol.oleh.touristweathercomparer.model;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import ua.in.khol.oleh.touristweathercomparer.model.db.DatabaseHelper;
import ua.in.khol.oleh.touristweathercomparer.model.location.LocationHelper;
import ua.in.khol.oleh.touristweathercomparer.model.preferences.PreferencesHelper;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherHelper;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherProvider;
import ua.in.khol.oleh.touristweathercomparer.utils.LocaleUnits;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Place;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public class GodRepository implements Repository {
    private static final int DB_LOCATION_ACCURACY = 2; // TODO move this to PreferencesHelper

    private Context mAppContext;
    private final LocationHelper mLocationHelper;
    private final WeatherHelper mWeatherHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final DatabaseHelper mDatabaseHelper;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private PublishSubject<Status> mStatusPublicSubject = PublishSubject.create();
    private ReplaySubject<Location> mLocationSubject = ReplaySubject.create();

    public GodRepository(Application application,
                         LocationHelper locationHelper, WeatherHelper weatherHelper,
                         PreferencesHelper preferencesHelper, DatabaseHelper databaseHelper) {
        mAppContext = application.getApplicationContext();
        mLocationHelper = locationHelper;
        mWeatherHelper = weatherHelper;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public Observable<Status> observeStatus() {
        return mStatusPublicSubject.serialize();
    }

    @Override
    public void clearStatus() {
        mStatusPublicSubject.onNext(Status.CLEAR);
    }

    @Override
    public void update() {
        loadPreferences();
        updateConfiguration();

        processLocation();
    }

    private void processLocation() {
        mCompositeDisposable.add(mLocationHelper.observeLocationUsable()
                .filter(locationUsable -> {
                    if (!locationUsable)
                        mStatusPublicSubject.onNext(Status.LOCATION_UNAVAILABLE);
                    return locationUsable;
                })
                .flatMapSingle((Function<Boolean, SingleSource<Location>>) aBoolean -> {
                    mStatusPublicSubject.onNext(Status.REFRESHING);
                    return mLocationHelper.observeLocation();

                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .doOnSuccess(location -> {
                    Place place = new Place(location.getLatitude(), location.getLongitude());
                    long placeId = mDatabaseHelper.getPlaceId(place, DB_LOCATION_ACCURACY);
                    if (placeId == 0)
                        mDatabaseHelper.putPlace(place, DB_LOCATION_ACCURACY);
                })
                .subscribe(location -> {
                    mStatusPublicSubject.onNext(Status.REFRESHED);
                    mLocationSubject.onNext(location);
                }, Throwable::printStackTrace)
        );
    }

    public Observable<City> observeCity() {
        return Observable.combineLatest(mLocationSubject,
                ReactiveNetwork.observeInternetConnectivity(),
                (location, connected) -> {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    long placeId = mDatabaseHelper
                            .getPlaceId(latitude, longitude, DB_LOCATION_ACCURACY);

                    City city;
                    if (connected) {
                        String name = mLocationHelper.getLocationName(latitude, longitude,
                                mPreferencesHelper.getLanguage());
                        city = new City(name, placeId);
                        long cityId = mDatabaseHelper.getCityId(placeId);
                        if (cityId == 0)
                            mDatabaseHelper.putCity(city, DB_LOCATION_ACCURACY);
                    } else { // Get city from DB
                        city = mDatabaseHelper.getCity(placeId);
                        if (city == null) {
                            mStatusPublicSubject.onNext(Status.CRITICAL_OFFLINE);
                            city = new City();
                        } else
                            mStatusPublicSubject.onNext(Status.OFFLINE);
                    }

                    return city;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public Observable<Forecast> observeForecast() {
        return Observable.combineLatest(mLocationSubject,
                ReactiveNetwork.observeInternetConnectivity(),
                (location, connected) -> {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    long placeId = mDatabaseHelper
                            .getPlaceId(latitude, longitude, DB_LOCATION_ACCURACY);
                    List<Forecast> forecastList = new ArrayList<>();

                    for (WeatherProvider provider : mWeatherHelper.getWeatherProviders()) {
                        if (connected) {
                            List<WeatherData> weatherDataList
                                    = provider.getWeatherDataList(latitude, longitude);
                            if (weatherDataList != null) {
                                for (WeatherData data : weatherDataList) {
                                    Forecast forecast = convertWeatherDataToForecast(data);
                                    forecast.setPlaceId(placeId);
                                    forecastList.add(forecast);
                                }
                                provider.setCached(true);
                            }
                            mDatabaseHelper.putForecastList(forecastList);
                        } else {
                            if (!provider.isCached()) {
                                DateTime dateTime = new DateTime().withTimeAtStartOfDay();
                                int date = (int) (dateTime.getMillis() / 1000);
                                forecastList
                                        .addAll(mDatabaseHelper.getForecastList(provider.getId(),
                                                placeId, date));
                                if (forecastList.size() == 0)
                                    mStatusPublicSubject.onNext(Status.CRITICAL_OFFLINE);
                                else
                                    mStatusPublicSubject.onNext(Status.OFFLINE);
                            }
                        }
                    }

                    return Observable.fromIterable(forecastList);
                })
                .flatMap((Function<Observable<Forecast>, ObservableSource<Forecast>>)
                        forecastObservable -> forecastObservable);
    }

    @Override
    public Observable<Provider> observeProvider() {
        return Observable.fromIterable(mWeatherHelper.getWeatherProviders())
                .flatMap((Function<WeatherProvider, ObservableSource<Provider>>) wp -> Observable
                        .just(new Provider(wp.getId(), wp.getSite(), wp.getBanner())));
    }

    @Override
    public Observable<Title> observeTitle() {
        return Observable.fromIterable(mWeatherHelper.getWeatherProviders())
                .flatMap((Function<WeatherProvider, ObservableSource<Title>>) wp -> Observable
                        .just(new Title(wp.getId(), wp.getName())));

    }

    private Forecast convertWeatherDataToForecast(WeatherData weatherData) {
        Forecast forecast = new Forecast(weatherData.getProviderId(),
                weatherData.getDate(),
                weatherData.getLow(),
                weatherData.getHigh(),
                weatherData.getText(),
                weatherData.getSrc(),
                weatherData.getHumidity());
        if (weatherData.isCurrent()) {
            forecast.setIsCurrent(true);
            forecast.setCurrent(weatherData.getCurrent());
            forecast.setCurrentText(weatherData.getTextExtra());
            forecast.setCurrentImage(weatherData.getSrcExtra());
        }

        return forecast;
    }

    @Override
    public void cancel() {
        mCompositeDisposable.dispose();
    }

    private void loadPreferences() {
        LocaleUnits.getInstance().setCelsius(mPreferencesHelper.getCelsius());
        LocaleUnits.getInstance().setLanguage(mPreferencesHelper.getLanguage());
    }

    @Override
    public void updateConfiguration() {
        String language = mPreferencesHelper.getLanguage();

        // TODO refactor
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = mAppContext.getResources().getConfiguration();
        config.locale = locale;
        mAppContext.getResources().updateConfiguration(config,
                mAppContext.getResources().getDisplayMetrics());
    }

    @Override
    public boolean getPrefCelsius() {
        return mPreferencesHelper.getCelsius();
    }

    @Override
    public int getPrefLanguageIndex() {
        return mPreferencesHelper.getLanguageIndex();
    }

    @Override
    public void putPrefCelsius(boolean celsius) {
        mPreferencesHelper.putCelsius(celsius);
    }

    @Override
    public void putPrefLanguageIndex(int index) {
        mPreferencesHelper.putLanguageIndex(index);
    }


    @Override
    public void updatePreferences() {
        loadPreferences();
        updateConfiguration();

        // Notify activity with "recreate" request
        mStatusPublicSubject.onNext(Status.NEED_RECREATE);
    }
}

//    @SuppressLint("CheckResult")
//    public Observable<Forecast> observeForecast() {
//        return Observable.combineLatest(mLocationSubject,
//                ReactiveNetwork.observeInternetConnectivity(),
//                (location, connected) -> {
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//
//                    Hourly<Observable<WeatherData>> observables = new ArrayList<>();
//
//                    for (WeatherProvider provider : mWeatherHelper.getWeatherProviders())
//                        observables.add(provider.observeWeatherData(latitude, longitude));
//
//                    return Observable.concat(observables);
//                })
//                .flatMap((Function<Observable<WeatherData>, ObservableSource<WeatherData>>) weatherDataObservable -> weatherDataObservable)
//                .map(weatherData -> {
//                    Forecast forecast = convertWeatherDataToForecast(weatherData);
//                    long cityId = mDatabaseHelper.getCityId(weatherData.getLatitude(),
//                            weatherData.getLongitude(), DB_LOCATION_ACCURACY);
//                    forecast.setCityId(cityId);
//
//                    return forecast;
//                })
//                .doOnNext(forecast -> {
//                    long id = mDatabaseHelper.getForecastId(forecast, DB_LOCATION_ACCURACY);
//                    if (id == 0)
//                        id = mDatabaseHelper.putForecast(forecast, DB_LOCATION_ACCURACY);
//                });
//    }
