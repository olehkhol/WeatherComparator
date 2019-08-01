package ua.in.khol.oleh.touristweathercomparer.presenters;

import ua.in.khol.oleh.touristweathercomparer.contracts.SettingsContract;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public class SettingsPresenter extends AbstractPresenter
        implements SettingsContract.Presenter, BasePresenter<SettingsContract.View> {

    private SettingsContract.View mView;

    public SettingsPresenter(Repository model) {
        mRepository = model;
    }

    @Override
    public void attachView(SettingsContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void onValuesChanged() {
        mRepository.update();
    }
}
