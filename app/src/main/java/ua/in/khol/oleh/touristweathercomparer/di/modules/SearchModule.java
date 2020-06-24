package ua.in.khol.oleh.touristweathercomparer.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import ua.in.khol.oleh.touristweathercomparer.di.subcomponents.SearchSubcomponent;
import ua.in.khol.oleh.touristweathercomparer.views.SearchView;

@Module(subcomponents = SearchSubcomponent.class)
public abstract class SearchModule {

    @Binds
    @IntoMap
    @ClassKey(SearchView.class)
    abstract AndroidInjector.Factory<?>
    bindSearchViewInjectorFactory(SearchSubcomponent.Factory factory);
}
