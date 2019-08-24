package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Title;

public class MainViewModel extends BaseViewModel {
    private final ObservableField<String> mCityName = new ObservableField<>();
    private final ObservableDouble mLatitude = new ObservableDouble();
    private final ObservableDouble mLongitude = new ObservableDouble();
    private final ObservableList<Title> mTitles = new ObservableArrayList<>();
    private final ObservableList<Provider> mProviders = new ObservableArrayList<>();
    private final MutableLiveData<Boolean> mIsRecreate = new MutableLiveData<>();

    public MainViewModel(Repository repository) {
        super(repository);
        getCompositeDisposable().add(getRepository()
                .getRefreshObservable()
                .subscribe(mIsRecreate::setValue));
    }

    @Override
    public void wakeUp() {
        getRepository().updateConfiguration();
    }

    public void processData() {
        if (!isRefreshed())
            if (!getIsRefreshing().get())
                subscribeCity();
    }

    private void subscribeCity() {
        setIsRefreshing(true);
        getCompositeDisposable().add(getRepository()
                .getCity()
                .doOnError(throwable -> setIsRefreshing(false))
                .doOnComplete(this::subscribeProvidersData)
                .subscribe(city -> {
                    mCityName.set(city.getName());
                    mLatitude.set(city.getLatitude());
                    mLongitude.set(city.getLongitude());
                }));
    }

    private void subscribeProvidersData() {
        getCompositeDisposable().add(getRepository()
                .getProvidersData()
                .doOnComplete(() -> {
                    setIsRefreshing(false);
                    setRefreshed(true);
                })
                .doOnError(throwable -> setIsRefreshing(false))
                .subscribe(providerData -> {
                    WeatherData weatherData = providerData.getWeatherDataList().get(0);
                    Title title = new Title(providerData.getName(), weatherData.getCurrent(),
                            weatherData.getTextExtra(), weatherData.getSrcExtra());
                    mTitles.add(title);
                    Provider provider = new Provider(providerData.getUrl(),
                            providerData.getBanner());
                    provider.setForecasts(providerDataToForecast(providerData));
                    mProviders.add(provider);
                }));
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


    public ObservableField<String> getCityName() {
        return mCityName;
    }

    public ObservableDouble getLatitude() {
        return mLatitude;
    }

    public ObservableDouble getLongitude() {
        return mLongitude;
    }

    public ObservableList<Title> getTitles() {
        return mTitles;
    }

    public ObservableList<Provider> getProviders() {
        return mProviders;
    }

    public MutableLiveData<Boolean> getIsRecreate() {
        return mIsRecreate;
    }

    public void setIsRecreate(Boolean isRecreate) {
        mIsRecreate.setValue(isRecreate);
    }
}
