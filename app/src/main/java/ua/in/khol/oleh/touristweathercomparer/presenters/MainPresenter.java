package ua.in.khol.oleh.touristweathercomparer.presenters;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ua.in.khol.oleh.touristweathercomparer.contracts.MainContract;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.location.data.CityLocation;
import ua.in.khol.oleh.touristweathercomparer.model.weather.ProviderData;
import ua.in.khol.oleh.touristweathercomparer.model.weather.WeatherData;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Title;

public class MainPresenter extends AbstractPresenter
        implements MainContract.Presenter, BasePresenter<MainContract.View> {

    private MainContract.View mView;
    private String mCityName;
    private CityLocation mCityLocation;
    private Disposable mLocationDisposable;
    private Disposable mCityNameDisposable;
    private Disposable mProvidersDataDisposable;
    private List<Title> mTitles;
    private List<Provider> mProviders;
    private boolean mRefreshed;
    private Disposable mRefreshDisposable;

    public MainPresenter(Repository repository) {
        super(repository);
        mTitles = new ArrayList<>();
        mProviders = new ArrayList<>();
    }

    @Override
    public void attachView(MainContract.View view) {
        mView = view;

        if (mRefreshed) {
            mView.showLocation(mCityLocation.getLatitude(), mCityLocation.getLongitude());
            mView.showCityName(mCityName);
            mView.showProviders(mProviders);
            mView.showTitles(mTitles);
        } else {
            processData();
        }

        mRefreshDisposable = mRepository.getRefreshObservable()
                .subscribe(update -> {
                    if (update)
                        mView.updateUI();
                });
    }


    @Override
    public void detachView() {
        mRefreshDisposable.dispose();
        mView = null;
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
                        mCityLocation = location;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null)
                            mView.showError("Location error!");
                    }

                    @Override
                    public void onComplete() {
                        mLocationDisposable.dispose();
                        if (mView != null)
                            mView.showLocation(mCityLocation.getLatitude(),
                                    mCityLocation.getLongitude());
                        subscribeCityName();
                        subscribeProvidersData();
                    }
                });
    }

    private void subscribeCityName() {
        mRepository.getCityName(mCityLocation.getLatitude(), mCityLocation.getLongitude())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCityNameDisposable = d;
                    }

                    @Override
                    public void onNext(String name) {
                        mCityName = name;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null)
                            mView.showError("Network error!");
                    }

                    @Override
                    public void onComplete() {
                        mCityNameDisposable.dispose();
                        if (mView != null)
                            mView.showCityName(mCityName);
                    }
                });
    }

    private void subscribeProvidersData() {

        mRepository.getProvidersData(mCityLocation.getLatitude(), mCityLocation.getLongitude())
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
                        if (mView != null)
                            mView.showError("Weather error!");
                    }

                    @Override
                    public void onComplete() {
                        mProvidersDataDisposable.dispose();
                        mRefreshed = true;
                        mView.showProviders(mProviders);
                        mView.showTitles(mTitles);
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

    @Override
    public void destroy() {
        if (mProvidersDataDisposable != null)
            mProvidersDataDisposable.dispose();
        if (mCityNameDisposable != null)
            mCityNameDisposable.dispose();
        if (mLocationDisposable != null)
            mLocationDisposable.dispose();
        if (mRefreshDisposable != null)
            mRefreshDisposable.dispose();
    }

    @Override
    public void onProviderClicked(int position) {
        if (mView != null)
            mView.scrollTo(position);
    }

}
