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
import ua.in.khol.oleh.touristweathercomparer.views.adapters.RecyclerAdapter;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.UpperAdapter;

public class HomeView extends Fragment {
    private HomeViewModel mHomeViewModel;

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;

    private RecyclerView mUpperRecycler;
    private RecyclerView mLowerRecycler;
    private UpperAdapter mUpperAdapter;

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
//        view.setBackgroundColor(ContextCompat
//                .getColor(requireContext(), R.color.toolbar_background));
        return view;

    }

    @Override
    public void onDestroyView() {
        mUpperAdapter.setOnBannerClickListener(null);
        mUpperRecycler.setAdapter(null);
        mLowerRecycler.setAdapter(null);

        super.onDestroyView();
    }

    private void initBindings(ViewHomeBinding binding) {
        mUpperRecycler = binding.upperRecycler;
        mUpperRecycler.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mUpperAdapter = new UpperAdapter();
        mUpperAdapter.setOnBannerClickListener(url -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        mUpperRecycler.setAdapter(mUpperAdapter);

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
