package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewHomeBinding;
import ua.in.khol.oleh.touristweathercomparer.di.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.HomeViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.ProviderAdapter;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.RecyclerAdapter;

public class HomeView extends Fragment {
    private HomeViewModel mHomeViewModel;

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;

    private RecyclerView mUpperRecycler;
    private RecyclerView mLowerRecycler;
    private ProviderAdapter mProviderAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        // Dagger injection
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewHomeBinding viewHomeBinding
                = ViewHomeBinding.inflate(inflater, container, false);
        mHomeViewModel = new ViewModelProvider(this, mViewModelProviderFactory)
                .get(HomeViewModel.class);
        viewHomeBinding.setViewModel(mHomeViewModel);

        initBindings(viewHomeBinding);

        View view = viewHomeBinding.getRoot();
        view.setBackgroundColor(ContextCompat
                .getColor(requireContext(), R.color.toolbar_background));
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        mHomeViewModel.start();
    }

    @Override
    public void onStop() {
        mHomeViewModel.stop();

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mProviderAdapter.setOnBannerClickListener(null);
        mUpperRecycler.setLayoutManager(null);
        mUpperRecycler.setAdapter(null);
        mUpperRecycler = null;

        mLowerRecycler.setLayoutManager(null);
        mLowerRecycler.setAdapter(null);
        mLowerRecycler = null;

        super.onDestroyView();
    }

    private void initBindings(ViewHomeBinding binding) {
        mUpperRecycler = binding.upperRecycler;
        mUpperRecycler.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mProviderAdapter = new ProviderAdapter();
        mProviderAdapter.setOnBannerClickListener(url -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        mUpperRecycler.setAdapter(mProviderAdapter);

        mLowerRecycler = binding.lowerRecycler;
        mLowerRecycler.setLayoutManager(new LinearLayoutManager(requireContext(),
                RecyclerView.HORIZONTAL, false));
        RecyclerAdapter<Title> lowerAdapter
                = new RecyclerAdapter<>(R.layout.title_item, ua.in.khol.oleh.touristweathercomparer.BR.title, null);
        lowerAdapter.setOnItemClickListener((position, item)
                -> mUpperRecycler.getLayoutManager().scrollToPosition(position));
        mLowerRecycler.setAdapter(lowerAdapter);
    }
}
