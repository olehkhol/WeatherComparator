package ua.in.khol.oleh.touristweathercomparer.di.subcomponents;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.AveragesView;

@Subcomponent
public interface AveragesSubcomponent extends AndroidInjector<AveragesView> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<AveragesView> {
    }
}
