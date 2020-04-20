package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.InfoViewSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.InfoView;

@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = InfoViewSubcomponent.class)
public abstract class InfoViewModule {

    @Binds
    @IntoMap
    @ClassKey(InfoView.class)
    abstract AndroidInjector.Factory<?>
    bindInfoViewInjectorFactory(InfoViewSubcomponent.Factory factory);
}
