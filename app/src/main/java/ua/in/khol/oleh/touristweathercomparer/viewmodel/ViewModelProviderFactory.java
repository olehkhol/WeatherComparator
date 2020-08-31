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
        } else if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            //noinspection unchecked
            return (T) new SearchViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(AlertViewModel.class)) {
            //noinspection unchecked
            return (T) new AlertViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(ForecastViewModel.class)) {
            //noinspection unchecked
            return (T) new ForecastViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(AveragesViewModel.class)) {
            //noinspection unchecked
            return (T) new AveragesViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(InfoViewModel.class)) {
            //noinspection unchecked
            return (T) new InfoViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(MapaViewModel.class)) {
            //noinspection unchecked
            return (T) new MapaViewModel(mRepository);
        }

        throw new RuntimeException("Wrong ViewModel " + modelClass.getName());
    }
}
