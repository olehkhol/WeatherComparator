package ua.in.khol.oleh.touristweathercomparer.ui.history;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;

import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;
import ua.in.khol.oleh.touristweathercomparer.ui.FragmentView;
import ua.in.khol.oleh.touristweathercomparer.ui.view.data.SpaceItemDecoration;

public class HistoryView extends FragmentView<HistoryViewModel> {

    private static final int INVALID_POSITION = -1;

    private HistoryViewModel historyViewModel;
    private final PlacesAdapter placesAdapter = new PlacesAdapter();
    private RecyclerView rvPlaces;
    private MaterialTextView searchHint;
    private final ItemTouchHelper.Callback placeTouchHelperCallback = new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            int viewHolderPosition = viewHolder.getAbsoluteAdapterPosition();
            targetPosition = target.getAbsoluteAdapterPosition();
            if (touchedPosition == INVALID_POSITION)
                touchedPosition = viewHolderPosition;
            placesAdapter.notifyItemMoved(viewHolderPosition, targetPosition);

            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                             int direction) {
            PagedList<Place> pagedPlaces = placesAdapter.getCurrentList();
            if (pagedPlaces == null || pagedPlaces.isEmpty())
                return;

            int viewHolderPosition = viewHolder.getAbsoluteAdapterPosition();
            Place place = pagedPlaces.get(viewHolderPosition);

            historyViewModel.remove(place);
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            PagedList<Place> pagedPlaces = placesAdapter.getCurrentList();
            if (pagedPlaces == null || pagedPlaces.isEmpty())
                return;

            if (touchedPosition >= 0 && targetPosition >= 0 && touchedPosition != targetPosition) {
                ArrayList<Place> places = new ArrayList<>();
                if (touchedPosition > targetPosition) {
                    places.addAll(pagedPlaces.subList(targetPosition, touchedPosition + 1));
                    Collections.reverse(places);
                } else
                    places.addAll(pagedPlaces.subList(touchedPosition, targetPosition + 1));

                historyViewModel.swap(places);
            }
        }
    };
    private final ItemTouchHelper placeTouchHelper = new ItemTouchHelper(placeTouchHelperCallback);
    private int touchedPosition = INVALID_POSITION;
    private int targetPosition = INVALID_POSITION;

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainApplication) requireActivity().getApplication()).getApplicationComponent().inject(this);
        super.onAttach(context);

        historyViewModel = getViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_history;
    }

    @Override
    protected void initView(View view) {
        rvPlaces = view.findViewById(R.id.rv_places);
        rvPlaces.setHasFixedSize(true);
        rvPlaces.setLayoutManager(new LinearLayoutManager(requireContext()));
        int space = getResources().getDimensionPixelSize(R.dimen.space_between_hourly_widgets);
        rvPlaces.addItemDecoration(new SpaceItemDecoration(space));
        rvPlaces.setAdapter(placesAdapter);
        searchHint = view.findViewById(R.id.search_hint);

        placeTouchHelper.attachToRecyclerView(rvPlaces);
    }

    @Override
    public void onResume() {
        super.onResume();

        historyViewModel.getPagedPlacesEvent().observe(this, places -> {
            rvPlaces.setVisibility(places.isEmpty() ? View.GONE : View.VISIBLE);
            searchHint.setVisibility(places.isEmpty() ? View.VISIBLE : View.GONE);

            placesAdapter.submitList(places);
            touchedPosition = targetPosition = INVALID_POSITION;
        });
    }
}