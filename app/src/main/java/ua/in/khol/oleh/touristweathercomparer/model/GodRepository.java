package ua.in.khol.oleh.touristweathercomparer.model;

import android.annotation.SuppressLint;
import android.util.Pair;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import ua.in.khol.oleh.touristweathercomparer.bus.Event;
import ua.in.khol.oleh.touristweathercomparer.bus.RxBus;
import ua.in.khol.oleh.touristweathercomparer.model.cache.CacheHelper;
import ua.in.khol.oleh.touristweathercomparer.model.db.DatabaseHelper;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Current;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Daily;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;
import ua.in.khol.oleh.touristweathercomparer.model.location.RxLocationHelper;
import ua.in.khol.oleh.touristweathercomparer.model.maps.MapsHelper;
import ua.in.khol.oleh.touristweathercomparer.model.places.PlacesHelper;
import ua.in.khol.oleh.touristweathercomparer.model.settings.RxPreferencesHelper;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherHelper;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;

import static ua.in.khol.oleh.touristweathercomparer.utils.Math.round;

public class GodRepository implements Repository {
    private static final int LATLON_ACCURACY = 4;
    private final static int DAYS = 6;

    private final RxBus mRxBus;

    private final CacheHelper mCache;
    private final DatabaseHelper mDatabase;
    private final WeatherHelper mWeather;

    private final RxLocationHelper mLocation;
    private final MapsHelper mMaps;
    private final RxPreferencesHelper mPreferences;
    private final PlacesHelper mPlaces;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final PublishSubject<LatLon> mLatLonSubj = PublishSubject.create();
    private final BehaviorSubject<Place> mPlaceSubj = BehaviorSubject.create();

    private Settings mSettings;

    @SuppressLint("CheckResult")
    public GodRepository(RxBus rxBus, CacheHelper cache, RxLocationHelper location, MapsHelper maps,
                         WeatherHelper weather, RxPreferencesHelper preferences,
                         DatabaseHelper database, PlacesHelper places) {
        mRxBus = rxBus;
        mPreferences = preferences;
        mSettings = mPreferences.getSettings();
        mLocation = location;
        mCache = cache;
        mDatabase = database;
        mWeather = weather;
        mMaps = maps;
        mPlaces = places;

        //noinspection ResultOfMethodCallIgnored
        observeLatLon()
                .observeOn(Schedulers.io())
                .flatMapMaybe(this::tryPlace)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(mPlaceSubj::onNext);
    }

    private Observable<LatLon> observeLatLon() {
        return mLatLonSubj
                .doOnNext(mPreferences::putLatLon);
    }

    /**
     * Gets the physical location of the device
     */
    @Override
    public void coldRefresh() {
        mCompositeDisposable.add(mLocation.tryLatLon()
                .doOnComplete(() -> mRxBus.send(Event.LOCATION_UNAVAILABLE))
                .doOnSuccess(mPreferences::putLatLon)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(mLatLonSubj::onNext, Throwable::printStackTrace));
    }

    /**
     * Uses a stored location data
     */
    @Override
    public void hotRefresh() {
        mLatLonSubj.onNext(mPreferences.getLatLon());
    }

    @Override
    public Observable<Object> observeBus() {
        return mRxBus.toObservable();
    }

    @Override
    public Observable<Place> observePlace() {
        return mPlaceSubj;
    }

    @Override
    public Observable<Place> observeLatestPlace() {
        return mDatabase.observeLatestPlace();
    }

    @Override
    public Maybe<LatLon> tryLatLon() {
        return Maybe.concat(mPreferences.tryLatLon(),
                mLocation.tryLatLon().doOnSuccess(mPreferences::putLatLon))
                .firstElement();
    }

    @Override
    public Maybe<Place> tryPlace(LatLon latLon) {
        String language = mPreferences.getLanguage();
        double latitude = round(latLon.getLat(), LATLON_ACCURACY);
        double longitude = round(latLon.getLon(), LATLON_ACCURACY);

        return Maybe.concat(
                mMaps.tryPlace(latitude, longitude, language)
                        .doOnSuccess(place -> place.setId(mDatabase.putPlace(place))),
                mDatabase.tryPlace(latitude, longitude, language))
                .doOnComplete(() -> mRxBus.send(Event.OFFLINE))
                .firstElement();
    }

    @Override
    public Maybe<Average> tryCurrent(long placeId) {
        int time = getTime();
        int date = getDate();

        return Maybe.concat(mCache.tryCurrents(placeId, time),
                mDatabase.seePlace(placeId)
                        .flatMapMaybe((Function<Place, MaybeSource<List<Current>>>) place ->
                                mWeather.tryCurrents(place, date))
                        .doOnComplete(() -> mRxBus.send(Event.OFFLINE))
                        .doOnSuccess(currents -> {
                            mCache.putCurrents(placeId, currents, time);
                            mDatabase.putCurrents(currents);
                        }),
                mDatabase.tryCurrents(placeId, date)
                        .doOnSuccess(currents -> mCache.putCurrents(placeId, currents, time)))
                .doOnComplete(() -> mRxBus.send(Event.CRITICAL_OFFLINE))
                .firstElement()
                .map(this::averageTheCurrents);
    }

    @Override
    public Maybe<List<Average>> tryDailies(long placeId) {
        int time = getTime();
        int date = getDate();

        return Maybe.concat(mCache.tryDailies(placeId, time),
                mDatabase.seePlace(placeId)
                        .flatMapMaybe((Function<Place, MaybeSource<List<Daily>>>) place ->
                                mWeather.tryDailies(place, date))
                        .doOnSuccess(dailies -> {
                            mCache.putDailies(placeId, dailies, time);
                            mDatabase.putDailies(dailies);
                        }),
                mDatabase.tryDailies(placeId, date)
                        .doOnSuccess(dailies -> mCache.putDailies(placeId, dailies, time)))
                .firstElement()
                .map(this::splitAndAverageTheDailies);
    }

    @Override
    public Settings getSettings() {
        return mSettings;
    }

    @Override
    public void setSettings(Settings settings) {
        boolean languageChanged = mSettings.getLanguageIndex() != settings.getLanguageIndex();
        mSettings = Settings.copy(settings);
        mPreferences.putSetting(mSettings);

        mRxBus.send(Event.NEED_RECREATE);

        if (languageChanged)
            hotRefresh();
    }

    @Override
    public void setLocation(LatLon latLon) {
        mLatLonSubj.onNext(latLon);
    }

    @Override
    public void processPlaceById(String placeId) {
        mCompositeDisposable.add(mPlaces.seeLatlon(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::setLocation));
    }

    /**
     * Produces the list of predicted place names according to a query
     *
     * @param query - expected name
     * @return - list of pairs of id and name
     */
    @Override
    public Single<List<Pair<String, String>>> predictPlaceNames(String query) {
        return mPlaces.seePlacesList(query);
    }

    @Override
    public Observable<List<Place>> observeLatestPlaces(int placesCount) {
        return mDatabase.observeLatestPlaces(placesCount);
    }

    private Average averageTheCurrents(List<Current> currents) {
        List<Average.Canape> canapes = new ArrayList<>();
        int size = currents.size();
        if (size == 0) return null;

        long time = 0;
        float temp = 0;
        float pressure = 0;
        float speed = 0;
        int degree = 0;
        int humidity = 0;

        for (int i = 0; i < size; i++) {
            Current current = currents.get(i);
            time += current.getTime();
            temp += current.getTemp();
            pressure += current.getPressure();
            speed += current.getSpeed();
            degree += current.getDegree();
            humidity += current.getHumidity();
            canapes.add(new Average.Canape(current.getText(), current.getImage()));
        }
        Average average = new Average((int) (time / size), temp / size, temp / size, canapes);
        average.setPressure((int) (pressure / size));
        average.setSpeed(speed / size);
        average.setDegree(degree / size);
        average.setHumidity(humidity / size);

        return average;
    }

    private List<Average> splitAndAverageTheDailies(List<Daily> dailies) {
        List<Average> averages = new ArrayList<>();

        for (List<Daily> list : splitByDate(dailies)) {
            Average average = averageTheDailies(list);
            if (average != null)
                averages.add(average);
        }

        return averages;
    }

    private Average averageTheDailies(List<Daily> dailies) {
        List<Average.Canape> canapes = new ArrayList<>();
        int size = dailies.size();
        if (size == 0) return null;

        long date = 0;
        float low = 0;
        float high = 0;
        float pressure = 0;
        float speed = 0;
        int degree = 0;
        int humidity = 0;

        for (int i = 0; i < size; i++) {
            Daily daily = dailies.get(i);
            date += daily.getDate();
            low += daily.getLow();
            high += daily.getHigh();
            pressure += daily.getPressure();
            speed += daily.getSpeed();
            degree += daily.getDegree();
            humidity += daily.getHumidity();
            canapes.add(new Average.Canape(daily.getText(), daily.getImage()));
        }
        Average average = new Average((int) (date / size), low / size, high / size, canapes);
        average.setPressure((int) (pressure / size));
        average.setSpeed(speed / size);
        average.setDegree(degree / size);
        average.setHumidity(humidity / size);

        return average;
    }

    protected int getTime() {
        return (int) (new DateTime().getMillis() / 1000);
    }

    private int getDate() {
        return (int) (new DateTime().withTimeAtStartOfDay().getMillis() / 1000);
    }

    private List<List<Daily>> splitByDate(List<Daily> dailies) {
        List<List<Daily>> splitted = new ArrayList<>();
        DateTime dateTime = new DateTime()
                .withTimeAtStartOfDay();
        for (int i = 0; i < DAYS; i++) {
            List<Daily> list = new ArrayList<>();
            int date = (int) (dateTime.plusDays(i).getMillis() / 1000);
            for (Daily daily : dailies)
                if (daily.getDate() == date)
                    list.add(daily);
            splitted.add(list);
        }

        return splitted;
    }
}
