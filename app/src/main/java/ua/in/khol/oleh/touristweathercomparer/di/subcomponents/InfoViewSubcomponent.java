package ua.in.khol.oleh.touristweathercomparer.di.subcomponents;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.InfoView;

@Subcomponent
public interface InfoViewSubcomponent extends AndroidInjector<InfoView> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<InfoView> {
    }
}
