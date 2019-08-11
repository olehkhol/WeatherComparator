package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import java.util.ArrayList;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Title;

public class MainViewModel extends BaseViewModel {
    private ObservableField<String> mCityName = new ObservableField<>();
    private ObservableDouble mLatitude = new ObservableDouble();
    private ObservableDouble mLongitude = new ObservableDouble();
    private ObservableList<Title> mTitles = new ObservableArrayList<>();
    private ObservableList<Provider> mProviders = new ObservableArrayList<>();

    public MainViewModel(Repository repository) {
        super(repository);
    }

    public void processData() {
        subscribeLocation();
    }

    private void subscribeLocation() {
        setIsRefreshing(true);
        getCompositeDisposable().add(getRepository()
                .getSingleLocation()
                .doOnComplete(() -> {
                    subscribeCityName();
                    subscribeProvidersData();
                })
                .doOnError(throwable -> setIsRefreshing(false))
                .subscribe(cityLocation -> {
                    mLatitude.set(cityLocation.getLatitude());
                    mLongitude.set(cityLocation.getLongitude());
                }));
    }

    private void subscribeCityName() {
        getCompositeDisposable().add(getRepository()
                .getCityName(mLatitude.get(), mLongitude.get())
                .doOnError(throwable -> setIsRefreshing(false))
                .subscribe(name -> mCityName.set(name)));
    }

    private void subscribeProvidersData() {
        getCompositeDisposable().add(getRepository()
                .getProvidersData(mLatitude.get(), mLongitude.get())
                .doOnComplete(() -> setIsRefreshing(false))
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
}
