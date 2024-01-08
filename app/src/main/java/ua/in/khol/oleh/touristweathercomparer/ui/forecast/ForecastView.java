package ua.in.khol.oleh.touristweathercomparer.ui.forecast;

import static ua.in.khol.oleh.touristweathercomparer.Globals.GEOLOCATION_FORMAT;
import static ua.in.khol.oleh.touristweathercomparer.Globals.LOCATION_FORMAT;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.circularreveal.CircularRevealLinearLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ua.in.khol.oleh.touristweathercomparer.Globals;
import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;
import ua.in.khol.oleh.touristweathercomparer.ui.FragmentView;
import ua.in.khol.oleh.touristweathercomparer.ui.average.AverageView;

public class ForecastView extends FragmentView<ForecastViewModel> {

    private ForecastViewModel forecastViewModel;
    private AveragesAdapter averagesAdapter;
    private ViewPager2 vpAverages;
    private LinearLayout forecastLayout;
    private MaterialTextView searchHint;
    private final List<Place> places = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainApplication) requireActivity().getApplication())
                .getApplicationComponent().inject(this);
        super.onAttach(context);

        forecastViewModel = getViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_forecast;
    }

    @Override
    protected void initView(View view) {
        averagesAdapter = new AveragesAdapter(this);
        TabLayout tlPlaces = view.findViewById(R.id.tl_places);
        vpAverages = view.findViewById(R.id.vp_averages);
        forecastLayout = view.findViewById(R.id.forecast_layout);
        searchHint = view.findViewById(R.id.search_hint);
        vpAverages.setAdapter(averagesAdapter);
        new TabLayoutMediator(
                tlPlaces,
                vpAverages,
                (tab, position) -> tab.setCustomView(averagesAdapter.getTabView(position))
        ).attach();
    }

    @Override
    public void onResume() {
        super.onResume();

        forecastViewModel.getPlacesEvent().observe(this, list -> {
            forecastLayout.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
            searchHint.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            places.clear();
            places.addAll(list);
            averagesAdapter.notifyDataSetChanged();
        });
    }

    private class AveragesAdapter extends FragmentStateAdapter {

        public AveragesAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new AverageView();
            Bundle arguments = new Bundle();

            arguments.putSerializable(Globals.PLACE, places.get(position));
            fragment.setArguments(arguments);

            return fragment;
        }

        @Override
        public long getItemId(int position) {
            return places.get(position).id;
        }

        @Override
        public boolean containsItem(long itemId) {
            for (Place place : places)
                if (place.id == itemId)
                    return true;

            return false;
        }

        @Override
        public int getItemCount() {
            return places.size();
        }

        public View getTabView(final int position) {
            final Place place = places.get(position);
            View view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_tab, null, false);

            ((MaterialTextView) view.findViewById(R.id.mtv_tab_item_name))
                    .setText(place.name);
            ((MaterialTextView) view.findViewById(R.id.mtv_tab_item_location))
                    .setText(String.format(
                            new Locale(place.language),
                            LOCATION_FORMAT,
                            place.latitude,
                            place.longitude
                    ));
            CircularRevealLinearLayout llcTabItem = view.findViewById(R.id.llc_tab_item);
            llcTabItem.setOnClickListener(v -> vpAverages.setCurrentItem(position, true));
            llcTabItem.setOnLongClickListener(v -> {
                String uri = String.format(place.language, GEOLOCATION_FORMAT, place.latitude, place.longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                if (intent.resolveActivity(requireContext().getPackageManager()) != null)
                    startActivity(intent);

                return true;
            });

            return view;
        }
    }
}