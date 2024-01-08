package ua.in.khol.oleh.touristweathercomparer.ui.settings;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.ViewModelProviderFactory;

public class PreferenceView extends PreferenceFragmentCompat {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainApplication) requireActivity().getApplication())
                .getApplicationComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.view_preference, rootKey);
    }
}