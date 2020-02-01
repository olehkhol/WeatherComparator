package ua.in.khol.oleh.touristweathercomparer.di;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.views.HomeView;

@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = HomeViewSubcomponent.class)
abstract class HomeViewModule {

    @Binds
    @IntoMap
    @ClassKey(HomeView.class)
    abstract AndroidInjector.Factory<?>
    bindHomeViewInjectorFactory(HomeViewSubcomponent.Factory factory);
}
