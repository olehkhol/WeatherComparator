package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.AlertViewSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.AlertView;

@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = AlertViewSubcomponent.class)
public abstract class AlertViewModule {

    @Binds
    @IntoMap
    @ClassKey(AlertView.class)
    abstract AndroidInjector.Factory<?>
    bindAlertLocationViewInjectorFactory(AlertViewSubcomponent.Factory factory);
}
