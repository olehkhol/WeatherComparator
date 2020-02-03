package ua.in.khol.oleh.touristweathercomparer.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.MapaView;

@Subcomponent
public interface MapViewSubcomponent extends AndroidInjector<MapaView> {

    @Subcomponent.Factory
    public interface Factory extends AndroidInjector.Factory<MapaView> {
    }
}
