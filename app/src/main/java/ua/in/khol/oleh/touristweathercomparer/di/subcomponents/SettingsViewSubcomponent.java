package ua.in.khol.oleh.touristweathercomparer.di.subcomponents;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.views.SettingsView;

@Subcomponent
public interface SettingsViewSubcomponent extends AndroidInjector<SettingsView> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<SettingsView> {
    }
}
