package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.ForecastSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.ForecastView;

@Module(subcomponents = ForecastSubcomponent.class)
public abstract class ForecastModule {

    @Binds
    @IntoMap
    @ClassKey(ForecastView.class)
    abstract AndroidInjector.Factory<?>
    bindForecastViewInjectorFactory(ForecastSubcomponent.Factory factory);
}
