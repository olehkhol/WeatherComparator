package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewForecastBinding;
import ua.in.khol.oleh.touristweathercomparer.di.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ForecastViewModel;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.AverageAdapter;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.CanapeAdapter;

public class ForecastView extends Fragment implements ViewBinding<ViewForecastBinding> {

    @Inject
    ViewModelProviderFactory mFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewForecastBinding binding = ViewForecastBinding.inflate(inflater, container, false);
        initBinding(binding);

        return binding.getRoot();
    }

    @Override
    public void initBinding(ViewForecastBinding binding) {
        ForecastViewModel viewModel = new ViewModelProvider(this, mFactory)
                .get(ForecastViewModel.class);
        binding.setForecastViewModel(viewModel);
        binding.current.currentCanapesRecycler
                .setLayoutManager(new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false));
        binding.current.currentCanapesRecycler.setAdapter(new CanapeAdapter());
        binding.averagesRecycler.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false));
        binding.averagesRecycler.setAdapter(new AverageAdapter());
    }
}