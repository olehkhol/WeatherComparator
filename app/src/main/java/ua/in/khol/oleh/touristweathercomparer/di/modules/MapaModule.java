package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.MapaSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.MapaView;

@Module(subcomponents = MapaSubcomponent.class)
public abstract class MapaModule {

    @Binds
    @IntoMap
    @ClassKey(MapaView.class)
    abstract AndroidInjector.Factory<?>
    bindMapaViewInjectorFactory(MapaSubcomponent.Factory factory);
}
