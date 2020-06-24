package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;

public class SettingsViewModel extends BaseViewModel {

    // Data Binding
    private final Settings mSettings;
    private boolean mChanged = false;

    public SettingsViewModel(Repository repository) {
        super(repository);

        mSettings = repository.getSettings();
    }

    @Override
    protected void onCleared() {
        if (mChanged)
            getRepository().setSettings(mSettings);
        super.onCleared();
    }

    @Override
    public void refresh() {

    }

    public void onOkButtonClicked() {
        mChanged = true;
    }

    public Settings getSettings() {
        return mSettings;
    }
}