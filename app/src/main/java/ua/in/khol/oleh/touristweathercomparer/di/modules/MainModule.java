package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.MainSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.MainView;

@Module(subcomponents = MainSubcomponent.class)
public abstract class MainModule {

    @Binds
    @IntoMap
    @ClassKey(MainView.class)
    abstract AndroidInjector.Factory<?>
    bindMainViewInjectorFactory(MainSubcomponent.Factory factory);
}
