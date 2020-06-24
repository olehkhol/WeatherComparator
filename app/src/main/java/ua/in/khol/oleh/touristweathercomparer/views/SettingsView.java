package ua.in.khol.oleh.touristweathercomparer.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewSettingsBinding;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.SettingsViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ViewModelProviderFactory;

public class SettingsView extends DialogFragment
        implements ViewBinding<ViewSettingsBinding> {

    private SettingsViewModel mViewModel;

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;

    public SettingsView() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        // instantiate a viewmodel from the injected factory
        mViewModel = new ViewModelProvider(this, mViewModelProviderFactory)
                .get(SettingsViewModel.class);
    }

    static SettingsView newInstance() {
        SettingsView fragment = new SettingsView();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ViewSettingsBinding binding = ViewSettingsBinding
                .inflate(requireActivity().getLayoutInflater());
        binding.setSettingsViewModel(mViewModel);
        initBinding(binding);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(binding.getRoot())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.onOkButtonClicked();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null)
                            dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void initBinding(ViewSettingsBinding binding) {
        ArrayAdapter tempAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.temperature, R.layout.spinner_item);
        binding.tempSpinner.setAdapter(tempAdapter);
        ArrayAdapter presAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.pressure, R.layout.spinner_item);
        binding.presSpinner.setAdapter(presAdapter);
        ArrayAdapter windAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.speed, R.layout.spinner_item);
        binding.windSpinner.setAdapter(windAdapter);
        ArrayAdapter langAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.languages, R.layout.spinner_item);
        binding.langSpinner.setAdapter(langAdapter);
    }

}
