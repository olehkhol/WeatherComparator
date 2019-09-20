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
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import ua.in.khol.oleh.touristweathercomparer.model.db.DatabaseHelper;
import ua.in.khol.oleh.touristweathercomparer.model.location.LocationHelper;
import ua.in.khol.oleh.touristweathercomparer.model.preferences.PreferencesHelper;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherHelper;
import ua.in.khol.oleh.touristweathercomparer.utils.Calculation;
import ua.in.khol.oleh.touristweathercomparer.utils.LocaleUnits;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public class GodRepository implements Repository {

    private static final String TAG = GodRepository.class.getName();
    public static final int DB_LOCATION_ACCURACY = 2; // TODO move this to mPreferencesHelper

    public enum Status {
        OFFLINE, ONLINE, REFRESHING, REFRESHED, NEED_RECREATE, ERROR
    }

    private Context mAppContext;
    private final LocationHelper mLocationHelper;
    private final WeatherHelper mWeatherHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final DatabaseHelper mDatabaseHelper;
    private int mAccuracy;
    private int mPower;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private PublishSubject<Status> mStatusPublicSubject = PublishSubject.create();
    private ReplaySubject<Location> mLocationSubject = ReplaySubject.create();
    private PublishSubject<City> mCityPublishSubject = PublishSubject.create();
    private PublishSubject<Title> mTitlePublishSubject = PublishSubject.create();
    private PublishSubject<Provider> mProviderPublishSubject = PublishSubject.create();

    public GodRepository(Application application,
                         LocationHelper locationHelper, WeatherHelper weatherHelper,
                         PreferencesHelper preferencesHelper, DatabaseHelper databaseHelper) {
        mAppContext = application.getApplicationContext();
        mLocationHelper = locationHelper;
        mWeatherHelper = weatherHelper;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public Observable<Status> getRefreshObservable() {
        return mStatusPublicSubject.serialize();
    }

    @Override
    public Observable<City> getCity() {
        return mCityPublishSubject;
    }

    @Override
    public Observable<Title> getTitle() {
        return mTitlePublishSubject;
    }

    @Override
    public Observable<Provider> getProvider() {
        return mProviderPublishSubject;
    }

    @Override
    public void update() {
        updateMetricUnits();
        updateConfiguration();

        // TODO find better place for this line below
        mStatusPublicSubject.onNext(Status.REFRESHING);
        processLocation();
        processCity();
        processProviderData();
    }

    @Override
    public void cancel() {
        mCompositeDisposable.dispose();
    }

    @SuppressLint("CheckResult")
    private void processLocation() {
        mCompositeDisposable.add(mLocationHelper.getSingleLocation(mAccuracy, mPower)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(location -> mLocationSubject.onNext(location),
                        Throwable::printStackTrace));
    }

    @SuppressLint("CheckResult")
    private void processCity() {
        mCompositeDisposable.add(Observable.combineLatest(mLocationSubject,
                ReactiveNetwork.observeInternetConnectivity(), (location, connected) -> {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    City city;
                    if (connected) {
                        String name = mLocationHelper.getLocationName(latitude, longitude,
                                mPreferencesHelper.getLanguage());
                        city = new City(name, latitude, longitude);
                    } else { // Get city from DB
                        city = mDatabaseHelper.getCity(latitude, longitude, DB_LOCATION_ACCURACY);
                        if (city == null)
                            city = new City();
                    }

                    return city;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(city -> { // Put city into DB
                    long id = mDatabaseHelper.getCityId(city, DB_LOCATION_ACCURACY);
                    if (id == 0)
                        id = mDatabaseHelper.putCity(city, DB_LOCATION_ACCURACY);
                })
                .subscribe(city -> mCityPublishSubject.onNext(city), Throwable::printStackTrace));
    }

    private void processWeather() {

    }

    @SuppressLint("CheckResult")
    private void processProviderData() {
        Observable.combineLatest(mLocationSubject,
                ReactiveNetwork.observeInternetConnectivity(),
                new BiFunction<Location, Boolean, Observable<ProviderData>>() {
                    @Override
                    public Observable<ProviderData> apply(Location location, Boolean connected) throws Exception {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        double latitudeDb = Calculation.round(latitude, 2);
                        double longitudeDb = Calculation.round(longitude, 2);
                        String language = mPreferencesHelper.getLanguage();

                        if (connected)
                            return mWeatherHelper.observeProvidersData(latitude, longitude);
                        else
                            return mDatabaseHelper.observeProvidersData(latitude, longitude);
                    }
                })
                .switchMap(new Function<Observable<ProviderData>, ObservableSource<? extends ProviderData>>() {
                    @Override
                    public ObservableSource<? extends ProviderData> apply(Observable<ProviderData> providerDataObservable) throws Exception {
                        return providerDataObservable;
                    }
                })
                .doOnNext(new Consumer<ProviderData>() {
                    @Override
                    public void accept(ProviderData providerData) throws Exception {

                    }
                })
                .subscribe(new Consumer<ProviderData>() {
                    @Override
                    public void accept(ProviderData providerData) throws Exception {
                        // TITLE
                        WeatherData weatherData = providerData.getWeatherDataList().get(0);
                        long titleId = getIdByName(providerData.getName());
                        Title title = new Title(providerData.getName(), weatherData.getCurrent(), weatherData.getTextExtra(), weatherData.getSrcExtra());
                        // mDatabaseHelper.putTitle(title);
                        mTitlePublishSubject.onNext(title);

                        // PROVIDER
                        Provider provider = new Provider(providerData.getUrl(), providerData.getBanner());
                        // Put Provider into DB
                        // long providerId = mDatabaseHelper.putProvider(provider);


                        // FORECASTS
                        List<Forecast> forecasts = providerDataToForecast(providerData, 0);

                        // Put forecasts list to DB
                        // mDatabaseHelper.putForecasts(forecasts).subscribe();

                        provider.setForecasts(forecasts);
                        mProviderPublishSubject.onNext(provider);
                    }
                }, Throwable::printStackTrace);
    }

    private long getIdByName(String name) {
        for (int i = 0; i < mWeatherHelper.getWeatherProviders().size(); i++)
            if (mWeatherHelper.getWeatherProviders().get(i).getName().equals(name))
                return i;

        throw new IllegalArgumentException("Wrong provider name!");
    }

    private List<Forecast> providerDataToForecast(ProviderData providerData, long providerId) {
        List<Forecast> forecastList = new ArrayList<>();
        List<WeatherData> weatherDataList = providerData.getWeatherDataList();

        for (int i = 0; i < weatherDataList.size(); i++) {
            WeatherData weatherData = weatherDataList.get(i);
            Forecast forecast = new Forecast();
            forecast.setProviderId(providerId);
            forecast.setDate(weatherData.getDate());
            forecast.setLow(weatherData.getLow());
            forecast.setHigh(weatherData.getHigh());
            forecast.setText(weatherData.getText());
            forecast.setSrc(weatherData.getSrc());
            forecast.setHumidity(weatherData.getHumidity());
            forecastList.add(forecast);
        }

        return forecastList;
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

    @Override
    public void onPreferencesUpdate() {
        updateMetricUnits();
        updateConfiguration();

        // Notify activity with "recreate" request
        mStatusPublicSubject.onNext(Status.NEED_RECREATE);
    }

}
