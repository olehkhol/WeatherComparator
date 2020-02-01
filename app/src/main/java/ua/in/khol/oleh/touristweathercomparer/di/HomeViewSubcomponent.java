package ua.in.khol.oleh.touristweathercomparer.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.HomeView;

@Subcomponent
public interface HomeViewSubcomponent extends AndroidInjector<HomeView> {

    @Subcomponent.Factory
    public interface Factory extends AndroidInjector.Factory<HomeView> {
    }
}
