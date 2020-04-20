package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.MainViewSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.MainView;

@Module(includes = AndroidInjectionModule.class,
        subcomponents = MainViewSubcomponent.class)
public abstract class MainViewModule {

    @Binds
    @IntoMap
    @ClassKey(MainView.class)
    abstract AndroidInjector.Factory<?>
    bindMainViewInjectorFactory(MainViewSubcomponent.Factory factory);
}
