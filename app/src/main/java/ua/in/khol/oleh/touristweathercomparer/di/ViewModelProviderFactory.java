package ua.in.khol.oleh.touristweathercomparer.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.AlertLocationViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.MainViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.SettingsViewModel;

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
        } else if (modelClass.isAssignableFrom(AlertLocationViewModel.class)) {
            //noinspection unchecked
            return (T) new AlertLocationViewModel(mRepository);
        }

        throw new RuntimeException("Wrong ViewModel " + modelClass.getName());
    }
}
