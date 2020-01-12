package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public class SettingsViewModel extends BaseViewModel {

    public SettingsViewModel(Repository repository) {
        super(repository);
    }

    @Override
    public void update() {
        getRepository().updateConfiguration();
    }

    // TODO describe this method in an interface
    public void onValuesChanged() {
        getRepository().onPreferencesUpdate();
    }
}
