package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewSettingsBinding;
import ua.in.khol.oleh.touristweathercomparer.di.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.SettingsViewModel;

public class SettingsView extends Fragment {
    private SettingsViewModel mSettingsViewModel;

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;

    // -=-=-=-=-=-=-=-=[LIFECYCLE CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    public void onAttach(@NonNull Context context) {
        // Dagger injection
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        ViewSettingsBinding viewSettingsBinding
                = ViewSettingsBinding.inflate(inflater, container, false);
        mSettingsViewModel = new ViewModelProvider(this, mViewModelProviderFactory)
                .get(SettingsViewModel.class);
        viewSettingsBinding.setSettingsViewModel(mSettingsViewModel);

        // Add a background to the Fragment and return its Root View
        View view = viewSettingsBinding.getRoot();
        view.setBackgroundColor(ContextCompat
                .getColor(requireContext(), R.color.toolbar_background));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSettingsViewModel.start();
    }

    @Override
    public void onStop() {
        mSettingsViewModel.stop();
        super.onStop();
    }
    // ----------------[LIFECYCLE CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[VIEW CALLBACKS]=-=-=-=-=-=-=-=-
    // ----------------[VIEW CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[REGULAR METHODS]=-=-=-=-=-=-=-=-
    // ----------------[REGULAR METHODS]----------------
}
