package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewForecastBinding;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ForecastViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.AverageAdapter;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.CanapeAdapter;

public class ForecastView extends Fragment implements ViewBinding<ViewForecastBinding> {

    private ForecastViewModel mViewModel;

    @Inject
    ViewModelProviderFactory mFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        // Instantiate a viewmodel from the injected factory
        mViewModel = new ViewModelProvider(this, mFactory)
                .get(ForecastViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewForecastBinding binding = ViewForecastBinding.inflate(inflater, container, false);
        binding.setForecastViewModel(mViewModel);
        initBinding(binding);

        return binding.getRoot();
    }

    @Override
    public void initBinding(ViewForecastBinding binding) {
        Context context = binding.getRoot().getContext();
        //int orientation = context.getResources().getConfiguration().orientation
        //        == Configuration.ORIENTATION_PORTRAIT
        //        ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL;
        int orientation = RecyclerView.VERTICAL;
        LinearLayoutManager manager = new LinearLayoutManager(context, orientation, false);
        //manager.setReverseLayout(true);
        //manager.setStackFromEnd(true);
        binding.current.currentCanapesRecycler.setLayoutManager(manager);
        binding.current.currentCanapesRecycler.setAdapter(new CanapeAdapter());
        binding.averagesRecycler.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        binding.averagesRecycler.setAdapter(new AverageAdapter());
    }
}