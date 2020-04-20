package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.ForecastViewSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.ForecastView;

@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = ForecastViewSubcomponent.class)
public abstract class ForecastViewModule {

    @Binds
    @IntoMap
    @ClassKey(ForecastView.class)
    abstract AndroidInjector.Factory<?>
    bindForecastViewInjectorFactory(ForecastViewSubcomponent.Factory factory);
}
