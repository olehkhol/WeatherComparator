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
import ua.in.khol.oleh.touristweathercomparer.model.cache.RxCache;
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
    private final RxCache mRxCache;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private PublishSubject<Status> mStatusPublicSubject = PublishSubject.create();
    private ReplaySubject<Place> mPlaceSubject = ReplaySubject.create();

    public GodRepository(Application application,
                         LocationHelper locationHelper, WeatherHelper weatherHelper,
                         PreferencesHelper preferencesHelper, DatabaseHelper databaseHelper) {
        mAppContext = application.getApplicationContext();
        mLocationHelper = locationHelper;
        mWeatherHelper = weatherHelper;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
        mRxCache = new RxCache();
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

        refresh();
    }

    private void refresh() {
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
                .subscribe(location -> {
                    // Put place into DB
                    Place place = new Place(location.getLatitude(), location.getLongitude());
                    long placeId = mDatabaseHelper.getPlaceId(place, DB_LOCATION_ACCURACY);
                    if (placeId == 0)
                        placeId = mDatabaseHelper.putPlace(place, DB_LOCATION_ACCURACY);
                    place.setId(placeId);
                    // Emit place with ID
                    mPlaceSubject.onNext(place);
                    // Emit status REFRESHED
                    mStatusPublicSubject.onNext(Status.REFRESHED);
                }, Throwable::printStackTrace)
        );
    }

    @Override
    public Observable<Place> observePlace() {
        return mPlaceSubject;
    }

    @Override
    public Observable<City> observeCity() {
        if (mRxCache.getCity() != null)
            return Observable.just(mRxCache.getCity());
        else
            return Observable.combineLatest(observePlace(),
                    ReactiveNetwork.observeInternetConnectivity(),
                    (place, connected) -> {
                        double latitude = place.getLatitude();
                        double longitude = place.getLongitude();
                        long placeId = place.getId();

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

                        mRxCache.setCity(city);

                        return city;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io());
    }

    @Override
    public Observable<Forecast> observeForecast() {
        if (mRxCache.getForecastList() != null)
            return Observable.fromIterable(mRxCache.getForecastList());
        else
            return Observable.combineLatest(observePlace(),
                    ReactiveNetwork.observeInternetConnectivity(),
                    (place, connected) -> {
                        double latitude = place.getLatitude();
                        double longitude = place.getLongitude();
                        long placeId = place.getId();
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

                        mRxCache.setForecastList(forecastList);

                        return Observable.fromIterable(forecastList);
                    })
                    .flatMap((Function<Observable<Forecast>, ObservableSource<Forecast>>)
                            forecastObservable -> forecastObservable);
    }

    @Override
    public List<Title> getTitleList() {
        return mWeatherHelper.getTitleList();
    }

    @Override
    public List<Provider> getProviderList() {
        return mWeatherHelper.getProviderList();
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
