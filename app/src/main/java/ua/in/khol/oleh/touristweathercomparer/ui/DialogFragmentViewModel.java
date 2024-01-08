package ua.in.khol.oleh.touristweathercomparer.ui;

import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsRepository;

public abstract class DialogFragmentViewModel extends FragmentViewModel {

    public DialogFragmentViewModel(SettingsRepository settingsRepository) {
        super(settingsRepository);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}