package ua.in.khol.oleh.touristweathercomparer.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.AlertLocationView;

@Subcomponent
public interface AlertLocationViewSubcomponent extends AndroidInjector<AlertLocationView> {

    @Subcomponent.Factory
    public interface Factory extends AndroidInjector.Factory<AlertLocationView> {

    }
}
