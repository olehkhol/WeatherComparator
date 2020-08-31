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
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewAveragesBinding;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.AveragesViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.AverageAdapter;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.CanapeAdapter;

public class AveragesView extends Fragment implements ViewBinding<ViewAveragesBinding> {
    public static final String PLACE_ID = "PLACE_ID";

    @Inject
    ViewModelProviderFactory mFactory;

    private AveragesViewModel mViewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);

        super.onAttach(context);

        mViewModel = new ViewModelProvider(this, mFactory)
                .get(AveragesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewAveragesBinding mBinding = ViewAveragesBinding.inflate(inflater, container, false);
        mBinding.setAveragesViewModel(mViewModel);
        initBinding(mBinding);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        mViewModel.setPlaceId(args.getLong(PLACE_ID));
    }

    @Override
    public void initBinding(ViewAveragesBinding binding) {
        Context context = binding.getRoot().getContext();
        int orientation = RecyclerView.VERTICAL;
        LinearLayoutManager manager = new LinearLayoutManager(context, orientation, false);
        binding.current.currentCanapesRecycler.setLayoutManager(manager);
        binding.current.currentCanapesRecycler.setAdapter(new CanapeAdapter());
        binding.averagesRecycler.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        binding.averagesRecycler.setAdapter(new AverageAdapter());
    }
}