package ua.in.khol.oleh.touristweathercomparer.ui.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;

public class PlacesAdapter extends PagedListAdapter<Place, PlaceViewHolder> {

    protected PlacesAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_history, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = getItem(position);

        if (place != null)
            holder.bindTo(place);
        else
            // Null defines a placeholder item - PagedListAdapter automatically
            // invalidates this row when the actual object is loaded from the
            // database.
            holder.clear();
    }

    private static final DiffUtil.ItemCallback<Place> DIFF_CALLBACK
            = new DiffUtil.ItemCallback<Place>() {
        // Place details may have changed if reloaded from the database,
        // but ID is fixed
        @Override
        public boolean areItemsTheSame(@NonNull Place oldItem, @NonNull Place newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Place oldItem, @NonNull Place newItem) {
            return oldItem.latitude == newItem.latitude
                    && oldItem.longitude == newItem.longitude;
        }
    };
}