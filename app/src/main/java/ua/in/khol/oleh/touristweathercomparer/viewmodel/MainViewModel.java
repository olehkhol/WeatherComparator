package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.location.data.CityLocation;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Title;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<Title>> mTitlesObservable = new MutableLiveData<>();
    private MutableLiveData<List<Provider>> mProvidersObservable = new MutableLiveData<>();
    private MutableLiveData<String> mCityName = new MutableLiveData<>();
    private MutableLiveData<CityLocation> mCityLocation = new MutableLiveData<>();
    private boolean mRefreshed;
    private Disposable mLocationDisposable;
    private Disposable mCityNameDisposable;
    private Disposable mProvidersDataDisposable;
    private Disposable mRefreshDisposable;

    private Repository mRepository;
    private List<Title> mTitles = new ArrayList<>();
    private List<Provider> mProviders = new ArrayList<>();

    public MainViewModel() {
    }

    public void setRepository(Repository repository) {
        mRepository = repository;
        processData();
    }

    private void processData() {
        mRefreshed = false;
        subscribeLocation();
    }

    private void subscribeLocation() {
        mRepository.getSingleLocation()
                .subscribe(new Observer<CityLocation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mLocationDisposable = d;
                    }

                    @Override
                    public void onNext(CityLocation location) {
                        mCityLocation.setValue(location);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        mLocationDisposable.dispose();
                        subscribeCityName();
                        subscribeProvidersData();
                    }
                });
    }

    private void subscribeCityName() {
        mRepository.getCityName(mCityLocation.getValue().getLatitude(),
                mCityLocation.getValue().getLongitude())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCityNameDisposable = d;
                    }

                    @Override
                    public void onNext(String name) {
                        mCityName.setValue(name);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        mCityNameDisposable.dispose();
                    }
                });
    }

    private void subscribeProvidersData() {

        mRepository.getProvidersData(mCityLocation.getValue().getLatitude(),
                mCityLocation.getValue().getLongitude())
                .subscribe(new Observer<ProviderData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mProvidersDataDisposable = d;
                    }

                    @Override
                    public void onNext(ProviderData providerData) {
                        WeatherData weatherData = providerData.getWeatherDataList().get(0);
                        Title title = new Title(providerData.getName(), weatherData.getCurrent(),
                                weatherData.getTextExtra(), weatherData.getSrcExtra());
                        mTitles.add(title);
                        Provider provider = new Provider(providerData.getUrl(),
                                providerData.getBanner());
                        provider.setForecasts(providerDataToForecast(providerData));
                        mProviders.add(provider);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        mProvidersDataDisposable.dispose();
                        mRefreshed = true;
                        mTitlesObservable.setValue(mTitles);
                        mProvidersObservable.setValue(mProviders);
                    }
                });
    }

    private List<Forecast> providerDataToForecast(ProviderData providerData) {
        List<Forecast> forecastList = new ArrayList<>();
        List<WeatherData> weatherDataList = providerData.getWeatherDataList();

        for (int i = 0; i < weatherDataList.size(); i++) {
            WeatherData weatherData = weatherDataList.get(i);
            Forecast forecast = new Forecast();
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

    public MutableLiveData<String> getCityName() {
        return mCityName;
    }

    public MutableLiveData<CityLocation> getCityLocation() {
        return mCityLocation;
    }

    public MutableLiveData<List<Title>> getTitlesObservable() {
        return mTitlesObservable;
    }

    public MutableLiveData<List<Provider>> getProvidersObservable() {
        return mProvidersObservable;
    }
}
