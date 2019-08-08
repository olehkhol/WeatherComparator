package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.lifecycle.ViewModel;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public class SettingsViewModel extends ViewModel {

    private Repository mRepository;

    public SettingsViewModel() {
    }

    public void setRepository(Repository repository) {
        mRepository = repository;
    }

    public void onValuesChanged() {
        mRepository.update();
    }
}
