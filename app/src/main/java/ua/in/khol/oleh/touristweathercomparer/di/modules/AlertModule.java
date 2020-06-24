package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.AlertSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.AlertView;

@Module(subcomponents = AlertSubcomponent.class)
public abstract class AlertModule {

    @Binds
    @IntoMap
    @ClassKey(AlertView.class)
    abstract AndroidInjector.Factory<?>
    bindAlertLocationViewInjectorFactory(AlertSubcomponent.Factory factory);
}
