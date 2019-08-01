package ua.in.khol.oleh.touristweathercomparer.contracts;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.presenters.BasePresenter;
import ua.in.khol.oleh.touristweathercomparer.views.BaseView;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Title;

public interface MainContract {

    interface View extends BaseView<Presenter> {
        void showCityName(String cityName);

        void showLocation(double latitude, double longitude);

        void showTitles(List<Title> titles);

        void showProviders(List<Provider> providers);

        void showError(String message);

        void scrollTo(int position);

        void updateUI();
    }

    interface Presenter extends BasePresenter<View> {
        void onProviderClicked(int position);

    }

}
