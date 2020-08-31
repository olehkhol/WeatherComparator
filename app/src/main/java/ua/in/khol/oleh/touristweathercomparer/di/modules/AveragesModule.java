package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.AveragesSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.AveragesView;

@Module(subcomponents = AveragesSubcomponent.class)
public abstract class AveragesModule {

    @Binds
    @IntoMap
    @ClassKey(AveragesView.class)
    abstract AndroidInjector.Factory<?>
    bindAveragesViewInjectorFactory(AveragesSubcomponent.Factory factory);
}
