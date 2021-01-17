package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.TabItemBinding;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewForecastBinding;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ForecastViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Town;

import static ua.in.khol.oleh.touristweathercomparer.views.AveragesView.PLACE_ID;

public class ForecastView extends Fragment implements ViewBinding<ViewForecastBinding> {

    @Inject
    ViewModelProviderFactory mFactory;

    private ForecastViewModel mViewModel;
    private ForecastCollectionAdapter mAdapter;
    private final List<Town> mTowns = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);

        super.onAttach(context);

        mViewModel = new ViewModelProvider(this, mFactory)
                .get(ForecastViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewForecastBinding binding = ViewForecastBinding.inflate(inflater, container, false);
        binding.setForecastViewModel(mViewModel);
        initBinding(binding);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        mViewModel.getTowns().observe(this, towns -> {
            mTowns.clear();
            mTowns.addAll(towns);
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onStop() {
        mViewModel.getTowns().removeObservers(this);

        super.onStop();
    }

    @Override
    public void initBinding(ViewForecastBinding binding) {
        mAdapter = new ForecastCollectionAdapter(this);
        ViewPager2 mViewPager = binding.viewPager;
        mViewPager.setAdapter(mAdapter);

        new TabLayoutMediator(binding.tabLayout, mViewPager,
                (tab, position) -> tab.setCustomView(mAdapter.getTabView(position))).attach();
    }

    private class ForecastCollectionAdapter extends FragmentStateAdapter {

        public ForecastCollectionAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new AveragesView();
            Bundle args = new Bundle();
            Town town = mTowns.get(position);

            args.putLong(PLACE_ID, town.getPlaceId());
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public long getItemId(int position) {
            return mTowns.get(position).getPlaceId();
        }

        @Override
        public boolean containsItem(long itemId) {
            for (Town town : mTowns)
                if (town.getPlaceId() == itemId)
                    return true;

            return false;
        }

        @Override
        public int getItemCount() {
            return mTowns.size();
        }


        public View getTabView(int position) {
            TabItemBinding binding = DataBindingUtil
                    .inflate(getLayoutInflater(), R.layout.tab_item, null, true);

            binding.tabName.setText(mTowns.get(position).getName());
            binding.tabLatLon.setText(String.format(getString(R.string.formatted_latlon),
                    mTowns.get(position).getLatitude(), mTowns.get(position).getLongitude()));

            return binding.getRoot();
        }
    }
}