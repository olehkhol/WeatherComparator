package ua.in.khol.oleh.touristweathercomparer.di.subcomponents;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.MainView;

@Subcomponent
public interface MainViewSubcomponent extends AndroidInjector<MainView> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<MainView> {
    }

}
