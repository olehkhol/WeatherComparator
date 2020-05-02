package ua.in.khol.oleh.touristweathercomparer.model;

import android.annotation.SuppressLint;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import timber.log.Timber;
import ua.in.khol.oleh.touristweathercomparer.model.cache.CacheHelper;
import ua.in.khol.oleh.touristweathercomparer.model.db.DatabaseHelper;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.location.RxLocationHelper;
import ua.in.khol.oleh.touristweathercomparer.model.maps.MapsHelper;
import ua.in.khol.oleh.touristweathercomparer.model.net.RxNetwork;
import ua.in.khol.oleh.touristweathercomparer.model.settings.RxSettingsHelper;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherHelper;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;

public class GodRepository implements Repository {
    private static final int LATLON_ACCURACY = 4; // TODO move this to PreferencesHelper
    private final static int DAYS = 6; // TODO move this to PreferencesHelper
    private final static int HOUR = 60 * 60;
    private final MapsHelper mMapsHelper;
    private final WeatherHelper mWeatherHelper;
    private final DatabaseHelper mStoreHelper;
    private final CacheHelper mCacheHelper;
    private final RxSettingsHelper mSettingsHelper;
    private final RxLocationHelper mLocationHelper;
    private final RxNetwork mNetwork;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final PublishSubject<Boolean> mRefreshSubject = PublishSubject.create();
    private final PublishSubject<Status> mStatus = PublishSubject.create();
    private Settings mSettings;
    private final PublishSubject<Settings> mSettingsSubject = PublishSubject.create();
    private final PublishSubject<LatLon> mLatLonSubject = PublishSubject.create();
    private final ReplaySubject<Place> mPlace = ReplaySubject.createWithSize(1);
    private boolean mRefreshed = false;
    private int mCurrentsTimestamp;
    private int mDailiesTimestamp;

    public GodRepository(CacheHelper cacheHelper, RxLocationHelper locationHelper,
                         RxNetwork network, MapsHelper mapsHelper,
                         WeatherHelper weatherHelper, RxSettingsHelper settingsHelper,
                         DatabaseHelper storeHelper) {
        // Settings helper
        mSettingsHelper = settingsHelper;
        mSettingsHelper.setup(RxSettingsHelper.SHARED_PREFERENCES);
        mSettings = mSettingsHelper.getSettings();
        mSettingsSubject.onNext(mSettings);
        // Location helper
        mLocationHelper = locationHelper;
        LatLon latLon = new LatLon(mSettingsHelper.getLat(), mSettingsHelper.getLon());
        mLatLonSubject.onNext(latLon);
        // Network helper
        mNetwork = network;
        // Cache helpers
        mCacheHelper = cacheHelper;
        mStoreHelper = storeHelper;

        mMapsHelper = mapsHelper;
        mWeatherHelper = weatherHelper;

        subscribeRefresh();
        subscribeSettings();
        producePlace();
    }


    /**
     * Responds to the Refresh commands
     * <p>
     * coldRefresh() - a refresh command from the activity
     * hotRefresh() - a refresh command from the fragment
     * <p>
     * This subscription exists throughout the life of the repository.
     * There is no need to use the result of the subscription.
     */
    @SuppressLint("CheckResult")
    private void subscribeRefresh() {
        mRefreshSubject
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(required -> {
                    if (required)
                        coldRefresh();
                    else
                        hotRefresh();
                });
    }

    /**
     * Handles the change of settings
     * <p>
     * This subscription exists throughout the life of the repository.
     * There is no need to use the result of the subscription.
     */
    @SuppressLint("CheckResult")
    private void subscribeSettings() {
        observeSettings()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(settings -> {
                    mSettings = settings;
                    mSettingsHelper.putSetting(settings);
                    mStatus.onNext(Status.NEED_RECREATE);
                });
    }

    /**
     * Provides refresh of the device location
     */
    private void coldRefresh() {
        produceLatLon();
    }

    /**
     * Uses a cached location data
     */
    private void hotRefresh() {
        LatLon latLon;
        if ((latLon = mCacheHelper.getLatLon()) != null)
            getLatLonSubject().onNext(latLon);
    }

    /**
     * Produces a physical location data
     * <p>
     * Uses the Android LocationProvider or Google Play Services location API.
     */
    private void produceLatLon() {
        mCompositeDisposable.add(mLocationHelper.observeUsability()
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(locationUsable -> {
                    if (!locationUsable)
                        mStatus.onNext(Status.LOCATION_UNAVAILABLE);
                    return locationUsable;
                })
                .flatMapSingle((Function<Boolean, SingleSource<LatLon>>) usable
                        -> mLocationHelper.observeSingleLocation()
                        .subscribeOn(AndroidSchedulers.mainThread()))
                .doOnNext(latLon -> {
                    mSettingsHelper.putLat(latLon.getLat());
                    mSettingsHelper.putLon(latLon.getLon());
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<LatLon>() {
                    @Override
                    public void accept(LatLon latLon) throws Exception {
                        if (latLon == null) {
                            Timber.e("ERROR! Null location!");
                        } else
                            mLatLonSubject.onNext(latLon);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                })
        );
    }

    /**
     * Produces the Place variable required to get weather forecasts correctly
     * <p>
     * Uses in-memory data caching and database caching.
     * Fetches a time zone offset to lead the remote weather forecasts to a user time.
     */
    private void producePlace() {
        mCompositeDisposable.add(Observable.combineLatest(
                observeLatLon().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()),
                observeInternet().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()),
                (latLon, connected) -> {
                    Place place;
                    double lat = round(latLon.getLat(), LATLON_ACCURACY);
                    double lon = round(latLon.getLon(), LATLON_ACCURACY);
                    String lang = mSettingsHelper.getLanguage();

                    if ((place = mCacheHelper.getPlace(lat, lon, lang)) == null) {
                        if ((place = mStoreHelper.getPlace(lat, lon, lang)) == null)
                            if (connected) {
                                place = new Place(lat, lon);
                                place.setLanguage(lang);
                                // use reliable LatLon to determine the place name
                                String name = mMapsHelper.getLocationName(lat, lon, lang);
                                place.setName(name);
                                // use rounded LatLon for all others
                                int offset = mMapsHelper.getTimeZoneOffset(lat, lon,
                                        new DateTime().getMillis() / 1000);
                                place.setOffset(offset);
                                // insert Place into DB
                                long placeId = mStoreHelper.putPlace(place);
                                place.setId(placeId);
                            } else
                                mStatus.onNext(Status.CRITICAL_OFFLINE);
                        // put Place to cache
                        if (place != null && place.getId() > 0)
                            mCacheHelper.putPlace(place);
                    }

                    return place != null ? place : new Place();
                })
                .filter(place -> place.getId() > 0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(place -> {
                    Timber.d("Place id:%d:name:%s:lan:%s:off:%d",
                            place.getId(), place.getName(), place.getLanguage(), place.getOffset());
                    // update refresh status
                    if (!mRefreshed)
                        mRefreshed = true;
                    // emit new Place instance
                    mPlace.onNext(place);
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    private Observable<LatLon> observeLatLon() {
        return mLatLonSubject
                .doOnNext(mCacheHelper::putLatLon);
    }

    private Observable<Boolean> observeInternet() {
        return mNetwork.observeInternetConnectivity();
    }

    @Override
    public Observable<City> observeCity() {
        return observePlace()
                .map(place -> new City(place.getName(), place.getLatitude(), place.getLongitude()))
                .doOnSubscribe(disposable
                        -> Timber.d(".observeCity subscribed:%s", disposable.toString()))
                .doOnDispose(() -> Timber.d(".observeCity disposed"));
    }

    @Override
    public Observable<Average> observeCurrent() {
        return observeCurrents()
                .map(this::calculateAverage)
                .doOnSubscribe(disposable
                        -> Timber.d(".observeCurrent subscribed:%s", disposable.toString()))
                .doOnDispose(() -> Timber.d(".observeCurrent disposed"));
    }

    @Override
    public Observable<List<Average>> observeAverages() {
        return observeDailies()
                .map(lists -> {
                    List<Average> averages = new ArrayList<>();

                    for (List<Forecast> list : lists) {
                        Average average = calculateAverage(list);
                        if (average != null)
                            averages.add(average);
                    }

                    return averages;
                })
                .doOnSubscribe(disposable
                        -> Timber.d(".observeAverages subscribed:%s", disposable.toString()))
                .doOnDispose(() -> Timber.d(".observeAverages disposed"));
    }

    @Override
    public Observable<Settings> observeSettings() {
        return mSettingsSubject;
    }

    public Observable<Status> observeStatus() {
        return mStatus.serialize();
    }

    @Override
    public Observable<Place> observePlace() {
        return mPlace;
    }

    @Override
    public Observable<List<Forecast>> observeCurrents() {
        return Observable.combineLatest(
                observePlace().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()),
                observeInternet().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()),
                (place, connected) -> {
                    List<Forecast> currents;
                    long placeId = place.getId();
                    TimeZone timeZone = TimeZone.getDefault();
                    int offset = timeZone.getRawOffset() / 1000;
                    int saving = timeZone.getDSTSavings() / 1000;
                    int time = (int) (new DateTime().getMillis() / 1000);
                    int date = (int) (new DateTime()
                            .withTimeAtStartOfDay().plusSeconds(offset).getMillis() / 1000);

                    if (time - mCurrentsTimestamp > HOUR
                            || (currents = mCacheHelper.getCurrents(placeId, mCurrentsTimestamp)) == null) {
                        if (time - mCurrentsTimestamp > HOUR
                                || (currents = mStoreHelper.getCurrents(placeId, mCurrentsTimestamp)) == null) {
                            if (connected) {
                                currents = mWeatherHelper.getCurrents(place, time);
                                // insert list of Current into DB
                                if (!currents.isEmpty()) {
                                    mCurrentsTimestamp = time;
                                    mStoreHelper.putCurrents(currents);
                                }
                            } else {
                                mStatus.onNext(Status.OFFLINE);
                                // try to get old stored forecasts from DB
                                currents = mStoreHelper.getCurrents(placeId, date);
                            }
                        } else if (!currents.isEmpty())
                            mCacheHelper.putCurrents(placeId, currents);
                    }

                    return currents;
                })
                .filter(currents -> !currents.isEmpty());
    }

    @Override
    public Observable<List<List<Forecast>>> observeDailies() {
        return Observable.combineLatest(
                observePlace().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()),
                observeInternet().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()),
                (place, connected) -> {
                    List<Forecast> dailies;
                    long placeId = place.getId();
                    int time = (int) (new DateTime().getMillis() / 1000);
                    DateTime dateTime = new DateTime();
                    int date = (int) (dateTime.withTimeAtStartOfDay().getMillis() / 1000);

                    if (time - mDailiesTimestamp > HOUR
                            || (dailies = mCacheHelper.getDailies(placeId, date)) == null) {
                        if (time - mDailiesTimestamp > HOUR
                                || (dailies = mStoreHelper.getDailies(placeId, date)).size() == 0) {
                            if (connected) {
                                dailies = mWeatherHelper.getDailies(place, date);
                                // insert list of Dailies into DB
                                if (!dailies.isEmpty()) {
                                    mDailiesTimestamp = time;
                                    mStoreHelper.putDailies(dailies);
                                }
                            } else {
                                mStatus.onNext(Status.OFFLINE);
                                // try to get old stored forecasts from DB
                                dailies = mStoreHelper.getDailies(placeId, date);
                            }
                            // put list of Dailies to cache
                        } else if (!dailies.isEmpty())
                            mCacheHelper.putDailies(placeId, dailies);
                    }

                    return splitByDate(dailies);
                })
                .filter(new Predicate<List<List<Forecast>>>() {
                    @Override
                    public boolean test(List<List<Forecast>> lists) throws Exception {
                        return !lists.isEmpty();
                    }
                });
    }

    @Override
    public PublishSubject<Settings> getSettingsSubject() {
        return mSettingsSubject;
    }

    @Override
    public PublishSubject<LatLon> getLatLonSubject() {
        return mLatLonSubject;
    }

    @Override
    public PublishSubject<Boolean> getRefreshSubject() {
        return mRefreshSubject;
    }

    @Override
    public Settings getSettings() {
        return mSettings;
    }

    private List<List<Forecast>> splitByDate(List<Forecast> dailies) {
        List<List<Forecast>> splitted = new ArrayList<>();
        DateTime dateTime = new DateTime()
                .withTimeAtStartOfDay();
        for (int i = 0; i < DAYS; i++) {
            List<Forecast> list = new ArrayList<>();
            int date = (int) (dateTime.plusDays(i).getMillis() / 1000);
            for (Forecast daily : dailies)
                if (daily.getDate() == date)
                    list.add(daily);
            splitted.add(list);
        }

        return splitted;
    }

    private Average calculateAverage(List<Forecast> forecasts) {
        List<Average.Canape> canapes = new ArrayList<>();
        int size = forecasts.size();
        if (size == 0) return null;

        long date = 0;
        float low = 0;
        float high = 0;
        float pressure = 0;
        float speed = 0;
        int degree = 0;
        int humidity = 0;

        for (int i = 0; i < size; i++) {
            Forecast current = forecasts.get(i);
            date += current.getDate();
            low += current.getLow();
            high += current.getHigh();
            pressure += current.getPressure();
            speed += current.getSpeed();
            degree += current.getDegree();
            humidity += current.getHumidity();
            canapes.add(new Average.Canape(current.getText(), current.getImage()));
        }
        Average average = new Average((int) (date / size), low / size, high / size, canapes);
        average.setPressure((int) (pressure / size));
        average.setSpeed(speed / size);
        average.setDegree(degree / size);
        average.setHumidity(humidity / size);

        return average;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

//    private Observable<List<Forecast>> observeCurrentsCached(long placeId, int date, int time) {
//        List<Forecast> currents = new ArrayList<>();
//        if (time - mTimestamp < HOUR)
//            currents.addAll(mCacheHelper.getCurrents(placeId, date));
//
//        return Observable
//                .just(currents)
//                .filter(forecasts -> !forecasts.isEmpty());
//    }
//
//    private Observable<List<Forecast>> observeCurrentsStored(long placeId, int date, int time) {
//        List<Forecast> currents = new ArrayList<>();
//        if (time - mTimestamp < HOUR)
//            currents.addAll(mDatabaseHelper.getCurrents(placeId, date));
//
//        return Observable
//                .just(currents)
//                .filter(new Predicate<List<Forecast>>() {
//                    @Override
//                    public boolean test(List<Forecast> forecasts) throws Exception {
//                        return !forecasts.isEmpty();
//                    }
//                })
//                .doOnNext(new Consumer<List<Forecast>>() {
//                    @Override
//                    public void accept(List<Forecast> forecasts) throws Exception {
//                        mCacheHelper.putCurrents(placeId, forecasts);
//                    }
//                });
//    }
//
//    private Observable<List<Forecast>> observeCurrentsNetwork(Place place, int time) {
//        List<Forecast> currents = mWeatherHelper.getCurrents(place);
//
//        return Observable
//                .just(currents)
//                .filter(new Predicate<List<Forecast>>() {
//                    @Override
//                    public boolean test(List<Forecast> forecasts) throws Exception {
//                        return !forecasts.isEmpty();
//                    }
//                })
//                .doOnNext(new Consumer<List<Forecast>>() {
//                    @Override
//                    public void accept(List<Forecast> forecasts) throws Exception {
//                        mTimestamp = time;
//                        mDatabaseHelper.putCurrents(forecasts);
//                    }
//                });
//    }
