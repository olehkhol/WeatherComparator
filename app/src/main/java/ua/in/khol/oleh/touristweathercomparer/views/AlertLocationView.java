package ua.in.khol.oleh.touristweathercomparer.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.di.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.AlertLocationViewModel;

public class AlertLocationView extends DialogFragment {

    private AlertLocationViewModel mViewModel;


    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;

    public AlertLocationView() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        // Dagger injection
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    public static AlertLocationView newInstance() {
        return new AlertLocationView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alert_location_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this, mViewModelProviderFactory)
                .get(AlertLocationViewModel.class);

        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setTitle(requireContext().getString(R.string.dialog_location_required));
        alertDialogBuilder.setMessage(requireContext().getString(R.string.dialog_location_enable));
        alertDialogBuilder.setPositiveButton(requireContext().getString(R.string.ok),
                (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                });
        alertDialogBuilder.setNegativeButton(requireContext().getString(R.string.cancel),
                (dialog, which) -> dialog.cancel());

        return alertDialogBuilder.create();
    }

}
