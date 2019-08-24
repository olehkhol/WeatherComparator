package ua.in.khol.oleh.touristweathercomparer.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.MainView;

@Subcomponent
public interface MainViewSubcomponent extends AndroidInjector<MainView> {

    @Subcomponent.Factory
    public interface Factory extends AndroidInjector.Factory<MainView> {
    }

}
