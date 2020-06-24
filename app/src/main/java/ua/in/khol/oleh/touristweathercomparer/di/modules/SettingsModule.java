package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.SettingsSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.SettingsView;

@Module(subcomponents = SettingsSubcomponent.class)
public abstract class SettingsModule {

    @Binds
    @IntoMap
    @ClassKey(SettingsView.class)
    abstract AndroidInjector.Factory<?>
    bindSettingsViewInjectorFactory(SettingsSubcomponent.Factory factory);
}
