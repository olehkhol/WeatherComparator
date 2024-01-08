package ua.in.khol.oleh.touristweathercomparer.ui.map;

import static ua.in.khol.oleh.touristweathercomparer.ui.ProgressEvent.PROGRESS;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;
import ua.in.khol.oleh.touristweathercomparer.data.maps.MarkerData;
import ua.in.khol.oleh.touristweathercomparer.ui.FragmentView;
import ua.in.khol.oleh.touristweathercomparer.ui.widgets.MiniCurrentWidget;

public class MapView extends FragmentView<MapViewModel>
        implements OnMapReadyCallback {

    private MapViewModel mapViewModel;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private FragmentContainerView fragmentContainerView;
    private MiniCurrentWidget miniCurrentWidget;
    private CircularProgressIndicator cpiNamesPredicting;
    private static final LocationSource STUB_LOCATION_SOURCE = new LocationSource() {

        @Override
        public void activate(@NonNull LocationSource.OnLocationChangedListener listener) {
        }

        @Override
        public void deactivate() {
        }
    };
    private final GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {

        @Override
        public void onMapClick(@NonNull LatLng latLng) {
            mapViewModel.predictPlace(latLng.latitude, latLng.longitude);
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainApplication) requireActivity().getApplication())
                .getApplicationComponent().inject(this);
        super.onAttach(context);

        mapViewModel = getViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment.getMapAsync(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_map;
    }

    @Override
    protected void initView(View view) {
        fragmentContainerView = view.findViewById(R.id.map_fragment);
        miniCurrentWidget = view.findViewById(R.id.mini_current);
        cpiNamesPredicting = view.findViewById(R.id.cpi_predicting);
        mapFragment = SupportMapFragment.newInstance(new GoogleMapOptions());
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentContainerView.getId(), mapFragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //map.setMyLocationEnabled(true);
        // Stub LocationSource to avoid battery drain
        // because we already have the location pending handler
        map.setLocationSource(STUB_LOCATION_SOURCE);
        UiSettings mapUiSettings = map.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        mapUiSettings.setMapToolbarEnabled(true);
        mapUiSettings.setCompassEnabled(true);
        mapUiSettings.setMyLocationButtonEnabled(true);
        // Set listeners
        map.setOnMapClickListener(onMapClickListener);

        // Observe network data
        mapViewModel.getPlaceEvent().observe(this, this::createMarker);
        mapViewModel.getCurrentEvent().observe(this, this::displayCurrent);
    }

    @Override
    public void onResume() {
        super.onResume();

        mapViewModel.getProgressEvent().observe(this, progressEvent -> setProgressing(progressEvent == PROGRESS));
        mapViewModel.getCurrentEvent().observe(this, this::displayCurrent);
    }

    private void createMarker(Place place) {
        Marker lastSelectedMarker = map.addMarker(new MarkerOptions()
                .position(new LatLng(place.latitude, place.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        if (lastSelectedMarker != null) {
            String name = place.name;
            int endIndex = name.indexOf(',');
            if (endIndex > 0) {
                lastSelectedMarker.setTitle(name.substring(0, endIndex));
            } else {
                lastSelectedMarker.setTitle(name);
            }
            lastSelectedMarker.showInfoWindow();
            // Move to marker position
            LatLng latLng = new LatLng(place.latitude, place.longitude);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
        }
    }

    private void displayCurrent(Current current) {
        miniCurrentWidget.setCurrent(current);
        miniCurrentWidget.setVisibility(View.VISIBLE);
    }

    private void setProgressing(boolean isProgressing) {
        map.setOnMapClickListener(isProgressing ? null : onMapClickListener);
        fragmentContainerView.setAlpha(isProgressing ? 0.5f : 1f);
        cpiNamesPredicting.setVisibility(isProgressing ? View.VISIBLE : View.GONE);
    }

    // https://developers.google.com/maps/documentation/android-sdk/infowindows
    // https://github.com/googlemaps/android-samples/blob/master/ApiDemos/java/app/src/gms/java/com/example/mapdemo/MarkerDemoActivity.java
    private final class MarkerViewAdapter implements GoogleMap.InfoWindowAdapter {

        private final View contents;

        public MarkerViewAdapter() {
            contents = getLayoutInflater().inflate(R.layout.view_marker, null);
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            return null;
        }

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            render(marker, contents);

            return contents;
        }

        private void render(Marker marker, View view) {
            MarkerData data = getMarkerData(marker);
        }

        private MarkerData getMarkerData(Marker marker) {
            return null;
        }
    }
}