package ua.in.khol.oleh.touristweathercomparer.contracts;

import ua.in.khol.oleh.touristweathercomparer.presenters.BasePresenter;
import ua.in.khol.oleh.touristweathercomparer.views.BaseView;

public interface SettingsContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter<View> {
        void onValuesChanged();
    }
}
