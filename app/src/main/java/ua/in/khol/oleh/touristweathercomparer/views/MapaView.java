package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewMapaBinding;
import ua.in.khol.oleh.touristweathercomparer.di.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.MapaViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Place;

public class MapaView extends Fragment implements OnMapReadyCallback {

    private MapaViewModel mMapaViewModel;
    private GoogleMap mMap;

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        // Dagger injection
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        ViewMapaBinding viewMapaBinding
                = ViewMapaBinding.inflate(inflater, container, false);
        mMapaViewModel = new ViewModelProvider(this, mViewModelProviderFactory)
                .get(MapaViewModel.class);
        viewMapaBinding.setViewModel(mMapaViewModel);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return viewMapaBinding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMapaViewModel.getPlace().observe(getViewLifecycleOwner(), new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(""));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
            }
        });
    }
}
