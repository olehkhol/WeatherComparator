package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.MapaViewSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.MapaView;

@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = MapaViewSubcomponent.class)
public abstract class MapaViewModule {

    @Binds
    @IntoMap
    @ClassKey(MapaView.class)
    abstract AndroidInjector.Factory<?>
    bindMapaViewInjectorFactory(MapaViewSubcomponent.Factory factory);
}
