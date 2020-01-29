package ua.in.khol.oleh.touristweathercomparer.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.AlertView;

@Subcomponent
public interface AlertLocationViewSubcomponent extends AndroidInjector<AlertView> {

    @Subcomponent.Factory
    public interface Factory extends AndroidInjector.Factory<AlertView> {

    }
}
