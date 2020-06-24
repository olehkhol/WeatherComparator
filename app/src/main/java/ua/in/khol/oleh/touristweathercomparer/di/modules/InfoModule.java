package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.InfoSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.InfoView;

@Module(subcomponents = InfoSubcomponent.class)
public abstract class InfoModule {

    @Binds
    @IntoMap
    @ClassKey(InfoView.class)
    abstract AndroidInjector.Factory<?>
    bindInfoViewInjectorFactory(InfoSubcomponent.Factory factory);
}
