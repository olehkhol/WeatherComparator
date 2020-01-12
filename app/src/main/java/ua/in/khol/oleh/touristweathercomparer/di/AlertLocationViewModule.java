package ua.in.khol.oleh.touristweathercomparer.di;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.views.AlertLocationView;

@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = AlertLocationViewSubcomponent.class)
abstract class AlertLocationViewModule {

    @Binds
    @IntoMap
    @ClassKey(AlertLocationView.class)
    abstract AndroidInjector.Factory<?>
    bindAlertLocationViewInjectorFactory(AlertLocationViewSubcomponent.Factory factory);
}
