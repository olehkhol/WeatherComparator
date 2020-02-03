package ua.in.khol.oleh.touristweathercomparer.di;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.views.MapaView;

@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = MapViewSubcomponent.class)
abstract class MapViewModule {

    @Binds
    @IntoMap
    @ClassKey(MapaView.class)
    abstract AndroidInjector.Factory<?>
    bindMapViewInjectorFactory(MapViewSubcomponent.Factory factory);
}
