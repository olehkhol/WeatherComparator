package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public class SettingsViewModel extends BaseViewModel {

    public SettingsViewModel(Repository repository) {
        super(repository);
    }

    @Override
    public void wakeUp() {
        getRepository().updateConfiguration();
    }

    public void onValuesChanged() {
        getRepository().update();
    }
}
