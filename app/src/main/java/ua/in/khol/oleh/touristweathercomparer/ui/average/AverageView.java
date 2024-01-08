package ua.in.khol.oleh.touristweathercomparer.ui.average;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.Globals;
import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;
import ua.in.khol.oleh.touristweathercomparer.ui.FragmentView;
import ua.in.khol.oleh.touristweathercomparer.ui.view.data.SpaceItemDecoration;
import ua.in.khol.oleh.touristweathercomparer.ui.widgets.CurrentWidget;

public class AverageView extends FragmentView<AverageViewModel> {

    private AverageViewModel averageViewModel;
    private CurrentWidget currentWidget;
    private RecyclerView rvHourlies;
    private Place place;
    private AverageAdapter averageAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainApplication) requireActivity().getApplication())
                .getApplicationComponent().inject(this);
        super.onAttach(context);

        averageViewModel = getViewModel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            place = (Place) arguments.getSerializable(Globals.PLACE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (place != null) {
            averageViewModel.processPlace(place);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_average;
    }

    @Override
    protected void initView(View view) {
        Context context = requireContext();
        currentWidget = view.findViewById(R.id.mini_current);
        rvHourlies = view.findViewById(R.id.rv_hourlies);
        rvHourlies.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        int space = getResources().getDimensionPixelSize(R.dimen.space_between_hourly_widgets);
        rvHourlies.addItemDecoration(new SpaceItemDecoration(space));
        averageAdapter = new AverageAdapter();
        rvHourlies.setAdapter(averageAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        averageViewModel.getCurrentEvent().observe(this, this::displayCurrent);
        averageViewModel.getHourliesEvent().observe(this, this::displayHourlies);
    }

    private void displayCurrent(Current current) {
        currentWidget.setCurrent(current);
        currentWidget.setVisibility(View.VISIBLE);
    }

    private void displayHourlies(List<Hourly> hourlies) {
        averageAdapter.setHourlies(hourlies);
        rvHourlies.setVisibility(View.VISIBLE);
    }
}