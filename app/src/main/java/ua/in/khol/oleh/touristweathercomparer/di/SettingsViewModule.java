package ua.in.khol.oleh.touristweathercomparer.di;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.views.SettingsView;

@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = SettingsViewSubcomponent.class)
abstract class SettingsViewModule {

    @Binds
    @IntoMap
    @ClassKey(SettingsView.class)
    abstract AndroidInjector.Factory<?>
    bindSettingsViewInjectorFactory(SettingsViewSubcomponent.Factory factory);
}
