package ua.in.khol.oleh.touristweathercomparer.di.subcomponents;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.ForecastView;

@Subcomponent
public interface ForecastViewSubcomponent extends AndroidInjector<ForecastView> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<ForecastView> {
    }
}
