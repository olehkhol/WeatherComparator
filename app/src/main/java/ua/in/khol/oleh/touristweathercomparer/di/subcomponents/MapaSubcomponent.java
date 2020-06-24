package ua.in.khol.oleh.touristweathercomparer.di.subcomponents;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.MapaView;

@Subcomponent
public interface MapaSubcomponent extends AndroidInjector<MapaView> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<MapaView> {
    }
}
