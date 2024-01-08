package ua.in.khol.oleh.touristweathercomparer.ui.history;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.Locale;

import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;

import static ua.in.khol.oleh.touristweathercomparer.Globals.LOCATION_FORMAT;

public class PlaceViewHolder extends RecyclerView.ViewHolder {

    private final MaterialTextView mtvName;
    private final MaterialTextView mtvLocation;

    public PlaceViewHolder(@NonNull View itemView) {
        super(itemView);

        mtvName = itemView.findViewById(R.id.mtv_name);
        mtvLocation = itemView.findViewById(R.id.mtv_location);
    }

    public void bindTo(Place place) {
        mtvName.setText(place.name);
        mtvLocation.setText(String.format(
                new Locale(place.language),
                LOCATION_FORMAT,
                place.latitude,
                place.longitude
        ));
    }

    public void clear() {
        // todo - Keep this stub for the future
    }
}