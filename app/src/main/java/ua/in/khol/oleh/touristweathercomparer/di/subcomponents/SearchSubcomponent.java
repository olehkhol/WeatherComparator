package ua.in.khol.oleh.touristweathercomparer.di.subcomponents;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.SearchView;

@Subcomponent
public interface SearchSubcomponent extends AndroidInjector<SearchView> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<SearchView> {

    }
}
