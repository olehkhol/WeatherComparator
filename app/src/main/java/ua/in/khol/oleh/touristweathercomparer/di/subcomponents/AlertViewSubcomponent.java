package ua.in.khol.oleh.touristweathercomparer.di.subcomponents;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.AlertView;

@Subcomponent
public interface AlertViewSubcomponent extends AndroidInjector<AlertView> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<AlertView> {

    }
}
