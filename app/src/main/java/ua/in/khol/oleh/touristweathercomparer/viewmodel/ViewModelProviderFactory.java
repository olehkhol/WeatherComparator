package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository mRepository;

    public ViewModelProviderFactory(Repository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            //noinspection unchecked
            return (T) new MainViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            //noinspection unchecked
            return (T) new SettingsViewModel(mRepository);
        }

        throw new RuntimeException("Wrong ViewModel " + modelClass.getName());
    }
}
