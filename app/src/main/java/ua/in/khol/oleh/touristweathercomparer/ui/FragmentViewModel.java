package ua.in.khol.oleh.touristweathercomparer.ui;

import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsRepository;

public abstract class FragmentViewModel extends BaseViewModel {

    protected final SettingsRepository settingsRepository;

    public FragmentViewModel(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public String getLanguage() {
        return settingsRepository.retrieveLanguage();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}