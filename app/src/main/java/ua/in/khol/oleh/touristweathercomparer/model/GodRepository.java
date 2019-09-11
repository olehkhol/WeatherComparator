package ua.in.khol.oleh.touristweathercomparer.model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ua.in.khol.oleh.touristweathercomparer.model.db.DatabaseHelper;
import ua.in.khol.oleh.touristweathercomparer.model.location.LocationHelper;
import ua.in.khol.oleh.touristweathercomparer.model.preferences.PreferencesHelper;
import ua.in.khol.oleh.touristweathercomparer.model.weather.AbstractProvider;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.darksky.DarkSky;
import ua.in.khol.oleh.touristweathercomparer.model.weather.wwo.Wwo;
import ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo.Yahoo;
import ua.in.khol.oleh.touristweathercomparer.utils.Calculation;
import ua.in.khol.oleh.touristweathercomparer.utils.LocaleUnits;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Location;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public class GodRepository implements Repository {

    private static final String TAG = GodRepository.class.getName();

    public enum Status {
        NEED_CONNECTION, CONNECTED, REFRESHING, REFRESHED, NEED_RECREATE, ERROR
    }

    private Context mAppContext;
    private final LocationHelper mLocationHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final DatabaseHelper mDatabaseHelper;
    private int mAccuracy;
    private int mPower;
    private List<AbstractProvider> mWeatherProviders = new ArrayList<AbstractProvider>() {{
        add(new Yahoo());
        add(new DarkSky());
        add(new Wwo());
    }};
    private PublishSubject<Status> mStatusPublicSubject = PublishSubject.create();
    private PublishSubject<Location> mLocationPublishSubject = PublishSubject.create();
    private PublishSubject<City> mCityPublishSubject = PublishSubject.create();
    private PublishSubject<Title> mTitlePublishSubject = PublishSubject.create();
    private PublishSubject<Provider> mProviderPublishSubject = PublishSubject.create();

    public GodRepository(Application application, LocationHelper locationHelper,
                         PreferencesHelper preferencesHelper, DatabaseHelper databaseHelper) {
        mAppContext = application.getApplicationContext();
        mLocationHelper = locationHelper;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public Observable<Status> getRefreshObservable() {
        return mStatusPublicSubject.serialize();
    }

    @Override
    public Observable<Location> getLocation() {
        return mLocationPublishSubject;
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

    @SuppressLint("CheckResult")
    private void processLocation() {
        mLocationHelper.getSingleLocation(mAccuracy, mPower)
                .doOnSuccess(location -> {
                    Location cityLocation
                            = new Location(Calculation.round(location.getLatitude(), 2),
                            Calculation.round(location.getLongitude(), 2));
                    // Store location to DB
                    mDatabaseHelper
                            .putLocation(cityLocation)
                            .subscribeOn(Schedulers.newThread())
                            .doOnSuccess(locationId -> {
                                if (locationId == -1)// Record already exist in DB
                                    mDatabaseHelper.getLocationId(cityLocation.getLatitude(),
                                            cityLocation.getLongitude())
                                            .doOnSuccess(id -> {
                                                cityLocation.setId(id);
                                                mLocationPublishSubject.onNext(cityLocation);
                                            })
                                            .subscribe();
                                else {// Emit location to ModelView
                                    cityLocation.setId(locationId);
                                    mLocationPublishSubject.onNext(cityLocation);
                                }
                            })
                            .subscribe();
                })
                .subscribe();
    }

    @SuppressLint("CheckResult")
    private void processCity() {
        // TODO do something with disposable
        mLocationPublishSubject
                .doOnNext(location -> ReactiveNetwork.observeInternetConnectivity()
                        .filter(connected -> {
                            if (connected)
                                mStatusPublicSubject.onNext(Status.CONNECTED);
                            else {
                                mStatusPublicSubject.onNext(Status.NEED_CONNECTION);
                                // Get city from DB
                                long locationId = location.getId();
                                mDatabaseHelper.getCity(locationId)
                                        .subscribeOn(Schedulers.newThread())
                                        .doOnSuccess(city -> mCityPublishSubject.onNext(city))
                                        .subscribe();
                            }
                            return connected;
                        })
                        .take(1)
                        .doOnNext(aBoolean -> mLocationHelper
                                .getLocationName(location.getLatitude(), location.getLongitude(),
                                        mPreferencesHelper.getLanguage())
                                .doOnNext(cityName -> {
                                    City city = new City(cityName, location.getId());
                                    // Store city to DB
                                    mDatabaseHelper.putCity(city)
                                            .subscribeOn(Schedulers.newThread())
                                            .subscribe();

                                    mCityPublishSubject.onNext(city);
                                })
                                .subscribe())
                        .subscribe())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    private void processProviderData() {
        // TODO do something with disposable
        mLocationPublishSubject
                .doOnNext(new Consumer<Location>() {
                    @Override
                    public void accept(Location location) throws Exception {
                        ReactiveNetwork.observeInternetConnectivity()
                                .filter(connected -> {
                                    if (connected)
                                        mStatusPublicSubject.onNext(Status.CONNECTED);
                                    else {
                                        mStatusPublicSubject.onNext(Status.NEED_CONNECTION);
                                        // Get titles from DB
                                        mDatabaseHelper.getAllTitles(location.getId())
                                                .doOnSuccess(titles -> {
                                                    for (Title title : titles)
                                                        mTitlePublishSubject.onNext(title);
                                                })
                                                .subscribe();

                                    }
                                    return connected;
                                })
                                .take(1)
                                .switchMap(aBoolean -> {
                                    List<Observable<ProviderData>> observables = new ArrayList<>();
                                    for (AbstractProvider provider : mWeatherProviders)
                                        observables.add(provider.getWeatherObservable(location.getLatitude(), location.getLongitude()));
                                    return Observable.concat(observables);
                                })
                                .doOnNext(new Consumer<ProviderData>() {
                                    @Override
                                    public void accept(ProviderData providerData) throws Exception {

                                    }
                                })
                                .subscribe(new Observer<ProviderData>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(ProviderData providerData) {
                                        // TITLE
                                        WeatherData weatherData = providerData.getWeatherDataList().get(0);
                                        long titleId = getIdByName(providerData.getName());
                                        Title title = new Title(location.getId(), providerData.getName(), weatherData.getCurrent(), weatherData.getSrcExtra(), weatherData.getTextExtra());
                                        mDatabaseHelper.putTitle(title);


                                        mTitlePublishSubject.onNext(title);

                                        // PROVIDER
                                        Provider provider = new Provider(providerData.getUrl(), providerData.getBanner());
                                        // Put Provider into DB
                                        long providerId = mDatabaseHelper.putProvider(provider);


                                        // FORECASTS
                                        List<Forecast> forecasts
                                                = providerDataToForecast(providerData, location.getId(), providerId);

                                        // Put forecasts list to DB
                                        mDatabaseHelper.putForecasts(forecasts).subscribe();

                                        provider.setForecasts(forecasts);
                                        mProviderPublishSubject.onNext(provider);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mStatusPublicSubject.onNext(Status.ERROR);
                                    }

                                    @Override
                                    public void onComplete() {
                                        mStatusPublicSubject.onNext(Status.REFRESHED);
                                    }
                                });
                    }
                })
                .subscribe();
    }

    private long getIdByName(String name) {
        for (int i = 0; i < mWeatherProviders.size(); i++)
            if (mWeatherProviders.get(i).getName().equals(name))
                return i;

        throw new IllegalArgumentException("Wrong provider name!");
    }

    private List<Forecast> providerDataToForecast(ProviderData providerData, long locationId, long providerId) {
        List<Forecast> forecastList = new ArrayList<>();
        List<WeatherData> weatherDataList = providerData.getWeatherDataList();

        for (int i = 0; i < weatherDataList.size(); i++) {
            WeatherData weatherData = weatherDataList.get(i);
            Forecast forecast = new Forecast();
            forecast.setLocationId(locationId);
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
