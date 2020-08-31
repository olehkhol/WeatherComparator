package ua.in.khol.oleh.touristweathercomparer.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewAlertBinding;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.AlertViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ViewModelProviderFactory;

public class AlertView extends DialogFragment {

    private static final String TITLE = "TITLE";
    private static final String MESSAGE = "MESSAGE";
    private static final String ACTION = "ACTION";

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;

    public AlertView() {
    }

    static AlertView newInstance(String title, String message, String action) {
        AlertView fragment = new AlertView();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        args.putString(ACTION, action);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString(TITLE);
        String message = args.getString(MESSAGE);
        String action = args.getString(ACTION);

        ViewAlertBinding binding = ViewAlertBinding
                .inflate(requireActivity().getLayoutInflater());
        AlertViewModel viewModel = new ViewModelProvider(this, mViewModelProviderFactory)
                .get(AlertViewModel.class);
        viewModel.getTitle().set(title);
        viewModel.getMessage().set(message);
        binding.setAlertViewModel(viewModel);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(binding.getRoot())
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    Intent intent = new Intent(action);
                    AlertView.this.requireActivity().startActivity(intent);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    if (dialog != null)
                        dialog.dismiss();
                });

        return builder.create();
    }
}
